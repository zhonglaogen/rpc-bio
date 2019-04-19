import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import utils.Abst;

public class Test1 {
    public static void main(String[] args) {
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        System.out.println(marshallerFactory);
//        Abst abst = new Abst();
//        System.out.println(abst);
    }
}
