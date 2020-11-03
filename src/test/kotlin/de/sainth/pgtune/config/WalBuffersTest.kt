package de.sainth.pgtune.config

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.kotlintest.tables.row
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk

@MicronautTest
class WalBuffersTest() : DescribeSpec() {

    init {
        describe("WalBuffersTest") {
            forall(
                    row(Memory(1, SizeUnit.MB), Memory(32, SizeUnit.KB)),
                    row(Memory(1, SizeUnit.TB), Memory(16, SizeUnit.MB)),
                    row(Memory(100, SizeUnit.MB), Memory(3, SizeUnit.MB))
            ) { sharedBuffer, walBuffer: Memory ->
                val sharedBuffers = mockk<SharedBuffers>()
                every { sharedBuffers.sharedBuffers } returns sharedBuffer
                WalBuffers(sharedBuffers).walBuffers shouldBe walBuffer
            }
        }

    }

}