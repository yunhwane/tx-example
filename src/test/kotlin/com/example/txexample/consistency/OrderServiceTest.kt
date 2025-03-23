package com.example.txexample.consistency

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor


@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OrderServiceTest(
    private val orderService: OrderService,
    private val itemEntityRepository: ItemEntityRepository,
    private val orderEntityRepository: OrderEntityRepository
) {

    @Test
    fun `재고 부족으로 예외 발생시 주문과 재고 모두 롤백되어야 한다`() {
        val item = itemEntityRepository.save(ItemEntity(name = "샘플 상품", stock = 5))

        val exception = assertThrows(RuntimeException::class.java) {
            orderService.placeOrder(userId = 1L, itemId = item.id!!, quantity = 10)
        }

        println("예외 메시지: ${exception.message}")

        val reloadedItem = itemEntityRepository.findById(item.id!!).get()
        val allOrders = orderEntityRepository.findAll()

        assertEquals(5, reloadedItem.stock)
        assertTrue(allOrders.isEmpty())
    }
}