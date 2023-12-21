package br.ce.wcaquino.test;
import static br.ce.wcaquino.core.DriverFactory.getDriver;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import br.ce.wcaquino.core.DSL;
import br.ce.wcaquino.core.DriverFactory;

public class TestePrine {
	
	private DSL dsl;

	@Before
	public void inicializa(){
		dsl = new DSL();
	}
	
	@After
	public void finaliza(){
		DriverFactory.getDriver();
	}

	@Test
	public void deveInteragirComRadioPrime(){
		getDriver().get("https://www.primefaces.org/showcase/ui/input/oneRadio.xhtml");
		dsl.clicarRadio(By.xpath("//input[@id='j_idt287:line:1']/../..//span"));
		Assert.assertTrue(dsl.isRadioMarcado("j_idt287:line:1"));
		dsl.clicarRadio(By.xpath("//label[.='Option3']/..//span"));
		Assert.assertTrue(dsl.isRadioMarcado("j_idt287:line:2"));
	}
	
	@Test
	public void deveInteragirComSelectPrime(){
		getDriver().get("https://www.primefaces.org/showcase/ui/input/oneMenu.xhtml");
		dsl.selecionarComboPrime("j_idt286:option", "Option2");
		Assert.assertEquals("Option2", dsl.obterTexto("j_idt286:option_label"));
	}
}
