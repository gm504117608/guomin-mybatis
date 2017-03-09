package guonima.configuration;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

/**
 * Created by dell on 2017/3/7.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        // 这个运行时生成的动态代理对象是可以导出到文件的
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        String resource = "guonima/configuration/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //手动创建XMLConfigBuilder，并解析创建Configuration对象
        XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, null, null);
        Configuration configuration = parser.parse();
        //使用Configuration对象创建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        //使用MyBatis
        SqlSession sqlSession = sqlSessionFactory.openSession();

        PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);

        mapper.findAll();


    }

}
