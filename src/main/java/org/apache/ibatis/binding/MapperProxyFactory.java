/**
 * Copyright 2009-2015 the original author or authors.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ibatis.binding;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.session.SqlSession;

/**
 * 这个类负责创建具体Mapper接口代理对象的工厂类
 * @author Lasse Voss
 */
public class MapperProxyFactory<T> {

    // 具体Mapper接口的Class对象
    private final Class<T> mapperInterface;
    // 该接口下面方法的缓存 key是方法对象 value是对接口中方法对象的封装
    private final Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<Method, MapperMethod>();

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public Class<T> getMapperInterface() {
        return mapperInterface;
    }

    public Map<Method, MapperMethod> getMethodCache() {
        return methodCache;
    }

    @SuppressWarnings("unchecked")
    protected T newInstance(MapperProxy<T> mapperProxy) {
        // mapperInterface，说明Mapper接口被代理了，这样子返回的对象就是Mapper接口的子类，
        // 所有Mapper接口的方法被调用时都会被MapperProxy拦截,也就是执行MapperProxy.invoke()方法
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }

    /**
     * 在这里传入sqlSession 创建一个Mapper接口的代理类
     * @param sqlSession
     * @return
     */
    public T newInstance(SqlSession sqlSession) {
        //在这里创建了MapperProxy对象 这个类实现了JDK的动态代理接口 InvocationHandler
        final MapperProxy<T> mapperProxy = new MapperProxy<T>(sqlSession, mapperInterface, methodCache);
        //调用上面的方法 返回一个接口的代理类
        return newInstance(mapperProxy);
    }

}
