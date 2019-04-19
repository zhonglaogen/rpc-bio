package utils;

import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;


public class SerializableFactory4Marshalling {
    /**
     * 创建Jboss Marshalling 解码器Marshallingdecoder
     * @return MarshallingDecoder
     */
    public static MarshallingDecoder buildMarshallingDecoder(){
        //首先通过Marshalling工具类的精通方法获取Marshalling实例对象 参数serila标识创建的是java序列化工厂对象
        //jboss-marshalling-serial 包提供
        final MarshallerFactory marshallerFactory = Marshalling
                .getProvidedMarshallerFactory("serial");
        //创建了MarshallingConfiguration对象，配置了版本号为5
        final MarshallingConfiguration configuration=new MarshallingConfiguration();
        //序列化版本，只要使用jdk5以上版本，verson只能定义为5
        configuration.setVersion(5);
        //根据marshallerFactory和configuration创建provider
        UnmarshallerProvider provider=new DefaultUnmarshallerProvider(marshallerFactory,configuration);
        //创建netty的MarshallingDecoder对象，两个参数分别为provider和单个消息序列化后的最大长度
        MarshallingDecoder decoder=new MarshallingDecoder(provider,1024*1024*1);
        return decoder;
    }

    /**
     * 创建jboss Marshalling编码器MarshallEncoder
     * @return MarshalingEncoder
     */
    public static MarshallingEncoder buildMarshallingEncoder(){
        final MarshallerFactory marshallerFactory = Marshalling
                .getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration=new MarshallingConfiguration();
        configuration.setVersion(5);
        MarshallerProvider provider=new DefaultMarshallerProvider(marshallerFactory,configuration);
        //构建netty的MarshallingEncoder对象，MarshallingEncoder用于实现序列化接口的pojo对象序列化为二进制数组
        MarshallingEncoder encoder=new MarshallingEncoder(provider);
        return encoder;
    }
}
