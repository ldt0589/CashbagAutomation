package resource.common;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TestNGAnnotationTransformer implements IAnnotationTransformer {
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        GlobalVariables.RETRY_FAILED_TESTS = System.getProperty("retryFailedTests") == null ? "No" : System.getProperty("retryFailedTests");
        if (GlobalVariables.RETRY_FAILED_TESTS.equalsIgnoreCase("Yes")) {
            annotation.setRetryAnalyzer(TestNGRetryAnalyzer.class);
        }
    }
}
