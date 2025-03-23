package com.example.txexample.consistency

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service


@Service
class OrderService (
    private val itemRepository: ItemEntityRepository,
    private val orderRepository: OrderEntityRepository
){
    @Transactional  // 👉 이게 있어야 일관성 보장됨
    fun placeOrder(userId: Long, itemId: Long, quantity: Int) {
        val item = itemRepository.findById(itemId).orElseThrow()

        if (item.stock < quantity) {
            throw RuntimeException("재고 부족: 현재 재고=${item.stock}, 요청 수량=$quantity")
        }

        item.stock -= quantity
        itemRepository.save(item)

        val order = OrderEntity(userId = userId, itemId = itemId, quantity = quantity)
        orderRepository.save(order)
    }
}