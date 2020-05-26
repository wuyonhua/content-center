package com.itmuch.contentcenter.configuration;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;

@Slf4j
public class NacosWeightedRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        // 读取配置文件，并初始化 NacosWeightedRule。暂时用不上留空即可
    }

    @Override
    public Server choose(Object o) {
        try {
            // ILoadBalancer Ribbon的入口。
            // ILoadBalancer loadBalancer = this.getLoadBalancer();
            // 由于 ILoadBalancer接口 没有getName API。所以使用BaseLoadBalancer
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();

            // log.info("lb = {}", loadBalancer);

            // 想要请求的微服务名称。
            String name = loadBalancer.getName();

            // 实现负载均衡算法..
            // 基于自己写一个负载均衡算法还是有难度的。不过Nacos Client 已经内置了一个居于权重的负载均衡算法

            // 拿到服务发现的相关API
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();

            // nacos client自动通过基于权重的负载均衡算法，给我们选择一个实例。
            Instance instance = namingService.selectOneHealthyInstance(name);

            log.info("选择的实例是：port = {}, instance = {}", instance.getPort(), instance);

            return new NacosServer(instance);
        } catch (NacosException e) {
            return null;
        }
    }
}

// 问题：Nacos client内置了基于权重的负载均衡算法，那为什么Spring Cloud Alibaba还要再去整合Ribbon呢。
// 答：主要是为了符合Spring Cloud的标准。
// spring cloud 有个子项目是 spring cloud commons --> 定义了spring cloud标准
// spring cloud commons 有一个子项目是 spring cloud loadbalancer --> 定义了各种负载均衡器的标准，在这种标准里面没有权重概念。
// 所以Spring Cloud Alibaba遵循了这个标准，然后整合Ribbon。