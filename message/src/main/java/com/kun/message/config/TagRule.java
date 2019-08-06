package com.kun.message.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReflectUtil;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class TagRule extends AbstractLoadBalancerRule {
    @Autowired
    DiscoveryClient discoveryClient;
    @Value("${eureka.instance.metadata-map.routingTag}")
    String routingTag;


    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }

    @Override
    public void setLoadBalancer(ILoadBalancer lb) {
        super.setLoadBalancer(lb);
    }

    @Override
    public Server choose(Object key) {
        ILoadBalancer loadBalancer = getLoadBalancer();
        if (loadBalancer == null) {
            return null;
        }
        Server resultServer = null;

        while (resultServer == null) {
            if (Thread.interrupted()) {
                return null;
            }
            //获取所有服务提供者信息
            List<Server> allServers = loadBalancer.getAllServers();
            if (allServers.size() == 0) {
                return null;
            } else {
                resultServer = allServers.get(0);
                //获取提供者的实例信息
                List<ServiceInstance> instances = discoveryClient.getInstances(resultServer.getMetaInfo().getServiceIdForDiscovery());
                //根据实例中 metadata.routingTag进行分组
                Map<String, List<ServiceInstance>> collect = instances.stream().collect(groupingBy(s -> s.getMetadata().get("routingTag")));
                //获取与当前消费者在同一个分片的服务生产者 by routingTag
                List<ServiceInstance> serviceInstances = collect.get(routingTag);
                List<Server> sameTagServers = new ArrayList<>();
                for (ServiceInstance serviceInstance : serviceInstances) {
                    for (Server server : allServers) {
                        InstanceInfo instanceInfo = (InstanceInfo) ReflectUtil.getFieldValue(server, "instanceInfo");
                        if (instanceInfo.getInstanceId().equalsIgnoreCase(((EurekaDiscoveryClient.EurekaServiceInstance) serviceInstance).getInstanceInfo().getInstanceId())) {
                            sameTagServers.add(server);
                        }
                    }
                }
                //随机选出一个
                if (CollUtil.isNotEmpty(sameTagServers)) {
                    resultServer = sameTagServers.get(RandomUtil.randomInt(0,sameTagServers.size()));
                }

            }
            if (resultServer == null) {
                /*
                 * The only time this should happen is if the server list were
                 * somehow trimmed. This is a transient condition. Retry after
                 * yielding.
                 */
                Thread.yield();
                continue;
            }

            if (resultServer.isAlive()) {
                return (resultServer);
            }

            // Shouldn't actually happen.. but must be transient or a bug.
            resultServer = null;
            Thread.yield();
        }
        return resultServer;
    }

}
