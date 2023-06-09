import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.text.DecimalFormat

import javax.imageio.ImageIO

import org.openqa.selenium.WebDriver

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import my.AShotWrapper
import my.DevicePixelRatioResolver
import ru.yandex.qatools.ashot.Screenshot
import ru.yandex.qatools.ashot.comparison.ImageDiff
import ru.yandex.qatools.ashot.comparison.ImageDiffer

  WebUI.openBrowser('google.com')
  WebUI.maximizeWindow()
// resolve file name under the target directory
File resolveScreenshotFile(String fileName) {
	Path projectDir = Paths.get(RunConfiguration.getProjectDir())
	Path reportDir = projectDir.resolve('Screenshots')
	Files.createDirectories(reportDir)
	Path pngFile = reportDir.resolve(fileName)
	return pngFile.toFile()
}

// get diff%
Double diffRatioPercent(ImageDiff diff) {
	boolean hasDiff = diff.hasDiff()
	if (!hasDiff) {
		return 0.0
	}
	int diffSize = diff.getDiffSize()
	int area = diff.getMarkedImage().getWidth() * diff.getMarkedImage().getHeight()
	Double diffRatio = diff.getDiffSize() / area * 100
	return diffRatio	
}

// ---------------------------------------------------------------------------------



WebDriver driver = DriverFactory.getWebDriver()
float dpr = DevicePixelRatioResolver.resolveDPR(driver)

// Take screenshot of the orignal site
def originalUrl = 'https://huynh-widgethub-essential.shqc.site/products/selling-plans-ski-wax'
WebUI.navigateToUrl(originalUrl)
WebUI.delay(10)
File original = resolveScreenshotFile('TC4_original.png')

AShotWrapper.saveEntirePageImage(DriverFactory.getWebDriver(), original, 1000, dpr)
WebUI.comment(">>> wrote the original image into ${original.toString()}")

WebUI.delay(10)
// Take screenshot of the mimic site
// kazurayam made a mimic of the original for demonstration purpose
def mimicUrl = 'https://huynh-widgethub-essential.shqc.site/products/selling-plans-ski-wax'
WebUI.navigateToUrl(mimicUrl)
WebUI.delay(10)
File mimic = resolveScreenshotFile('TC4_mimic.png')

AShotWrapper.saveEntirePageImage(driver, mimic, 1000, dpr)
WebUI.comment(">>> wrote the mimic image into ${mimic.toString()}")

// make AShot's ImageDiff
BufferedImage expectedImage = ImageIO.read(original)
BufferedImage actualImage   = ImageIO.read(mimic)
Screenshot expectedScreenshot = new Screenshot(expectedImage)
Screenshot actualScreenshot = new Screenshot(actualImage)

ImageDiff diff = new ImageDiffer().makeDiff(expectedScreenshot, actualScreenshot)

BufferedImage markedImage = diff.getMarkedImage()

DecimalFormat dformat = new DecimalFormat("##0.00")

// check how much difference found between the original and the mimic.
// if diff% exceed the criteria, then mark the test case FAILED
Double criteriaPercent = 3.0
Double diffRatioPercent = diffRatioPercent(diff)
if (diffRatioPercent > criteriaPercent) {
	KeywordUtil.markFailed("diffRatio=${dformat.format(diffRatioPercent)} exceeds criteria=${criteriaPercent}")
}

// save the diff image into file
File diffFile = resolveScreenshotFile("TC4_imageDiff.png")
ImageIO.write(markedImage, "PNG", diffFile)
WebUI.comment(">>> wrote the ImageDiff into ${diffFile.toString()}")


WebUI.closeBrowser()