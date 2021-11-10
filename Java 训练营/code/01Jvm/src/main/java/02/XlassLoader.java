import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class XlassLoader extends ClassLoader{
    public static void main(String[] args) throws Exception {
        String className = "Hello";
        String methodName = "hello";

        ClassLoader classLoader = new XlassLoader();

        Class<?> clazz = classLoader.loadClass(className);

        for (Method m: clazz.getDeclaredMethods()) {
            System.out.println(clazz.getSimpleName() + "." + m.getName());
        }

        Object instance = clazz.getDeclaredConstructor().newInstance();
        Method method = clazz.getMethod(methodName);
        method.invoke(instance);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        System.out.println(name);
        String suffix = ".xlass";
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(name + suffix);

        try {

            int length = inputStream.available();
            byte[] byteArray = new byte[length];
            inputStream.read(byteArray);

            byte[] classBytes = decode(byteArray);
            defineClass(name, classBytes, 0, classBytes.length);

            FileOutputStream fileOutputStream = new FileOutputStream("Hello.class");
            System.out.println("write file.");
            fileOutputStream.write(classBytes);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return super.findClass(name);
    }

    private static byte[] decode(byte[] byteArray) {
        byte[] targetArray = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            targetArray[i] = (byte) (255 - byteArray[i]);
        }
        return targetArray;
    }
}
