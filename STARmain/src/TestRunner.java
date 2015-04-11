import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

// basic test runner that runs through all of the Junit test cases
public class TestRunner {

  public static void main(String[] args) {

    Result result = JUnitCore.runClasses(StarTest.class);
    for (Failure failure : result.getFailures()) {
      System.out.println(failure.toString());
    }

  }

}
