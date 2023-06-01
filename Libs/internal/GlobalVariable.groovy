package internal

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.main.TestCaseMain


/**
 * This class is generated automatically by Katalon Studio and should not be modified or deleted.
 */
public class GlobalVariable {
     
    /**
     * <p>Profile default : Number of runs of test cases including re-runs</p>
     */
    public static Object NUMBER_OF_TESTCASE_RUNS
     
    /**
     * <p></p>
     */
    public static Object CURRENT_TESTCASE_ID
     

    static {
        try {
            def selectedVariables = TestCaseMain.getGlobalVariables("default")
			selectedVariables += TestCaseMain.getGlobalVariables(RunConfiguration.getExecutionProfile())
            selectedVariables += TestCaseMain.getParsedValues(RunConfiguration.getOverridingParameters())
    
            NUMBER_OF_TESTCASE_RUNS = selectedVariables['NUMBER_OF_TESTCASE_RUNS']
            CURRENT_TESTCASE_ID = selectedVariables['CURRENT_TESTCASE_ID']
            
        } catch (Exception e) {
            TestCaseMain.logGlobalVariableError(e)
        }
    }
}
