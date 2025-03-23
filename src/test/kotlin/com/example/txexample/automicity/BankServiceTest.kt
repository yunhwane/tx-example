package com.example.txexample.automicity


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor


@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BankServiceTest(
    private val accountRepository: AccountRepository,
    private val bankService: BankService
) {

    @Test
    fun `예외 발생시 전체 롤백되어야 한다`() {
        val a = accountRepository.save(AccountEntity(name = "A", balance = 10000))
        val b = accountRepository.save(AccountEntity(name = "예외유발", balance = 5000))

        val exception = assertThrows<RuntimeException> {
            bankService.transfer(a.id!!, b.id!!, 3000)
        }

        println("예외 메시지: ${exception.message}")

        // 롤백 여부 검증
        val updatedA = accountRepository.findById(a.id!!).get()
        val updatedB = accountRepository.findById(b.id!!).get()

        assertEquals(10000, updatedA.balance)
        assertEquals(5000, updatedB.balance)
    }
}