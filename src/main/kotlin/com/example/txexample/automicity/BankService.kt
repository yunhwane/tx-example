package com.example.txexample.automicity

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class BankService(
    private val accountRepository: AccountRepository
) {

    @Transactional
    fun transfer(fromId: Long, toId: Long, amount: Int) {
        val from = accountRepository.findById(fromId).orElseThrow()
        val to = accountRepository.findById(toId).orElseThrow()

        from.balance -= amount
        to.balance += amount

        if(to.name == "예외유발") {
            throw RuntimeException("입금자 이름이 예외유발 → 강제 예외 발생")
        }
    }
}