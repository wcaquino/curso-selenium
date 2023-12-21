package br.ce.wcaquino.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import br.ce.wcaquino.core.Propriedades.TipoExecucao;

public class DriverFactory {
	
//	private static WebDriver driver;
	private static ThreadLocal<WebDriver> threadDriver = new ThreadLocal<WebDriver>(){
		@Override
		protected synchronized WebDriver initialValue(){
			return initDriver();
		}
	};
	
	private DriverFactory() {}
	
	public static WebDriver getDriver(){
		return threadDriver.get();
	}
	
	@SuppressWarnings("rawtypes")
	public static WebDriver initDriver(){
		WebDriver driver = null;
		if(Propriedades.TIPO_EXECUCAO == TipoExecucao.LOCAL) {
			switch (Propriedades.BROWSER) {
				case FIREFOX: driver = new FirefoxDriver(); break;
				case CHROME: driver = new ChromeDriver(); break;
			}
		}
		if(Propriedades.TIPO_EXECUCAO == TipoExecucao.GRID) {
			AbstractDriverOptions cap = null;
			switch (Propriedades.BROWSER) {
				case FIREFOX: cap = new FirefoxOptions(); break;
				case CHROME: cap = new ChromeOptions(); break;
			}
			try {
				driver = new RemoteWebDriver(new URL("http://10.0.0.92:4444/wd/hub"), cap);
			} catch (MalformedURLException e) {
				System.err.println("Falha na conexão com o GRID");
				e.printStackTrace();
			}
		}
		if(Propriedades.TIPO_EXECUCAO == TipoExecucao.NUVEM) {
			AbstractDriverOptions cap = null;
			switch (Propriedades.BROWSER) {
				case FIREFOX: cap = new FirefoxOptions(); break;
				case CHROME: cap = new ChromeOptions(); break;
				case IE: cap = new EdgeOptions(); break;
			}
			cap.setPlatformName("Windows 11");
			cap.setBrowserVersion("latest");
			Map<String, Object> sauceOptions = new HashMap<>();
			sauceOptions.put("username", "SEU_USERNAME_AQUI");
			sauceOptions.put("accessKey", "SUA_CREDENCIAL_AQUI");
			sauceOptions.put("build", "selenium-build-Y2D7A2");
			sauceOptions.put("name", "Suite #1234");
			cap.setCapability("sauce:options", sauceOptions);
			try {
				URL url = new URL("https://ondemand.us-west-1.saucelabs.com:443/wd/hub");
				driver = new RemoteWebDriver(url, cap);
			} catch (MalformedURLException e) {
				System.err.println("Falha na conexão com a Saucelabs");
				e.printStackTrace();
			}
		}
		driver.manage().window().setSize(new Dimension(1200, 765));			
		return driver;
	}

	public static void killDriver(){
		WebDriver driver = getDriver();
		if(driver != null) {
			driver.quit();
			driver = null;
		}
		if(threadDriver != null) {
			threadDriver.remove();
		}
	}
}
