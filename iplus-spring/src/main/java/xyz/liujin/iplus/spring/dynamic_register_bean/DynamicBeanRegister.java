package xyz.liujin.iplus.spring.dynamic_register_bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DynamicBeanRegister {
    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;


    /**
     * 动态注册 bean
     * @param beanName
     * @param clazz
     */
    public void registerBean(String beanName, Class<?> clazz) {
        configurableApplicationContext.getBean(clazz);
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(clazz).getBeanDefinition();
        defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinition);
    }
}
