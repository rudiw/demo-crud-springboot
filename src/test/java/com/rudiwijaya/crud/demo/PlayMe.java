package com.rudiwijaya.crud.demo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author rudiwijaya
 *
 */
class PlayMe {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		final int n = 6;
		
		for (int i = n; i > 0; i--) {
			final int xIdx = i - 1;
			for (int j = 0; j < xIdx; j++) {
				System.out.print(" ");
			}
			final int yIdx = n - xIdx;
			for (int j = 0; j < yIdx; j++) {
				System.out.print("#");	
			}
			System.out.println();
		}
	}

}
