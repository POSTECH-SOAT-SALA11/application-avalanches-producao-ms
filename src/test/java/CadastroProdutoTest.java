import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/com/avalanches/resources/features",
        glue = "com.avalanches.stepdefinitions",
        plugin = {"pretty", "html:target/cucumber-reports"}
)
public class CadastroProdutoTest {
}
