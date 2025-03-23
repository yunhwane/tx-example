package com.example.txexample.consistency

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface ItemEntityRepository : JpaRepository<ItemEntity, Long> {
}