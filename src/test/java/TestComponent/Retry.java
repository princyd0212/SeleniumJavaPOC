package TestComponent;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {
int count = 0;
int maxTry = 1;
    /**
     * @param result The result of the test method that just ran.
     * @return
     */
    @Override
    public boolean retry(ITestResult result) {
        if(count<maxTry){
            count++;
            return true;
        }
        return false;
    }
}
