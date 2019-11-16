package com.muliyul.android_academy

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDateTime

fun main(args: Array<String>) {
	require(args.isNotEmpty()) { "Input must be a valid JSON formatted string" }
	val mapper = jacksonObjectMapper().apply {
		registerModule(JavaTimeModule())
	}
	val orders: List<OrdersAnalyzer.Order>? = try {
		mapper.readValue(args[0])
	} catch (t: Throwable) {
		null
	}

	requireNotNull(orders) { """
		Input must represent a list of orders in the format: [
			{
				orderId,
				creationDate,
				orderLines: [{productId, name, quantity, unitPrice}]
			}
		]
	""".trimIndent() }
	val analyzer = OrdersAnalyzer()
	mapper.writeValue(System.out, analyzer.totalDailySales(orders))
}

class OrdersAnalyzer {

	data class Order(val orderId: Int, val creationDate: LocalDateTime, val orderLines: List<OrderLine>)

	data class OrderLine(val productId: Int, val name: String, val quantity: Int, val unitPrice: BigDecimal)

	fun totalDailySales(orders: List<Order>): Map<DayOfWeek, Int> {
		val zeros = DayOfWeek.values().associate { it to 0 }.toMutableMap()
		return orders.fold(zeros) { acc, order ->
			val dayOfWeek = order.creationDate.dayOfWeek
			val totalQuantity = order.orderLines.sumBy { it.quantity }
			acc[dayOfWeek] = acc.getValue(dayOfWeek) + totalQuantity
			return@fold acc
		}
	}
}
