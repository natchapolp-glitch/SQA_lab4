import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.Calculator;

import org.junit.jupiter.api.Test;

class MyFirstJUnitJupiterTests {

	private final Calculator calculator = new Calculator();

	//TC01: firstNumber = 1, secondNumber = 1, expected result = 2
	@Test
	void addition() {
		assertEquals(2, calculator.add(1, 1));
	}
	
	//TC02: firstNumber = 1, secondNumber = 1, expected result = 3 => failed
	@Test
	void fail_example_addition () {
		assertEquals(3, calculator.add(1, 1));
	}
}