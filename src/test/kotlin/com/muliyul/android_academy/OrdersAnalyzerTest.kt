package com.muliyul.android_academy

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Assert.assertEquals
import java.time.DayOfWeek
import kotlin.test.Test
import kotlin.test.expect

class OrdersAnalyzerTest {
	private val analyzer = OrdersAnalyzer()
	private val mapper = jacksonObjectMapper().apply {
		registerModule(JavaTimeModule())
	}

	@Test
	fun `excercise example`() {
		val input = """
			[
    {
        "orderId": 554,
        "creationDate": "2017-03-25T10:35:20",
        "orderLines": [
            {"productId": 9872, "name": "Pencil", "quantity": 3, "unitPrice": 3.00}
        ]
    },
    {
        "orderId": 555,
        "creationDate": "2017-03-25T11:24:20",
        "orderLines": [
            {"productId": 9872, "name": "Pencil", "quantity": 2, "unitPrice": 3.00},
            {"productId": 1746, "name": "Eraser", "quantity": 1, "unitPrice": 1.00}
        ]
    },
    {
        "orderId": 453,
        "creationDate": "2017-03-27T14:53:12",
        "orderLines": [
            {"productId": 5723, "name": "Pen", "quantity": 4, "unitPrice": 4.22},
            {"productId": 9872, "name": "Pencil", "quantity": 3, "unitPrice": 3.12},
            {"productId": 3433, "name": "Erasers Set", "quantity": 1, "unitPrice": 6.15}
        ]
    },
    {
        "orderId": 431,
        "creationDate": "2017-03-20T12:15:02",
        "orderLines": [
            {"productId": 5723, "name": "Pen", "quantity": 7, "unitPrice": 4.22},
            {"productId": 3433, "name": "Erasers Set", "quantity": 2, "unitPrice": 6.15}
        ]
    },
    {
        "orderId": 690,
        "creationDate": "2017-03-26T11:14:00",
        "orderLines": [
            {"productId": 9872, "name": "Pencil", "quantity": 4, "unitPrice": 3.12},
            {"productId": 4098, "name": "Marker", "quantity": 5, "unitPrice": 4.50}
        ]
    }
]
		""".trimIndent()
		val orders: List<OrdersAnalyzer.Order> = mapper.readValue(input)
		val result = analyzer.totalDailySales(orders)

		expect(result.size) { 7 }
		assertEquals(
			result, mapOf(
				DayOfWeek.SUNDAY to 9,
				DayOfWeek.MONDAY to 17,
				DayOfWeek.TUESDAY to 0,
				DayOfWeek.WEDNESDAY to 0,
				DayOfWeek.THURSDAY to 0,
				DayOfWeek.FRIDAY to 0,
				DayOfWeek.SATURDAY to 6
			)
		)
	}

	@Test
	fun `empty order list should return map with 7 days of week`() {
		val result = analyzer.totalDailySales(emptyList())
		expect(result.size) { 7 }
		expect(result) {
			mapOf(
				DayOfWeek.SUNDAY to 0,
				DayOfWeek.MONDAY to 0,
				DayOfWeek.TUESDAY to 0,
				DayOfWeek.WEDNESDAY to 0,
				DayOfWeek.THURSDAY to 0,
				DayOfWeek.FRIDAY to 0,
				DayOfWeek.SATURDAY to 0
			)
		}
	}
}
