package de.sainth.pgtune

import io.kotlintest.specs.DescribeSpec
import io.kotlintest.shouldBe
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk

@MicronautTest
class SharedBuffersTest(@Client("/") private val client: RxHttpClient) : DescribeSpec() {

    init {
        describe("SharedBuffersTest") {
            it("when dbApplication == DESKTOP then sharedBuffers = ram / 16") {
                val systemConfiguration = mockk<SystemConfiguration>(relaxed = true)
                every { systemConfiguration.dbApplication } returns DbApplication.DESKTOP
                every { systemConfiguration.ram } returns Memory(16, SizeUnit.GB)
                (SharedBuffers(systemConfiguration).sharedBuffers == Memory(1, SizeUnit.GB)) shouldBe true
            }
            it("when dbApplication != DESKTOP then sharedBuffers = ram / 4") {
                val systemConfiguration = mockk<SystemConfiguration>(relaxed = true)
                val applications = listOf(DbApplication.WEB, DbApplication.OLTP, DbApplication.DATA_WAREHOUSE, DbApplication.MIXED)
                every { systemConfiguration.dbApplication } returnsMany applications
                every { systemConfiguration.ram } returns Memory(16, SizeUnit.GB)
                applications.forEach {
                    (SharedBuffers(systemConfiguration).sharedBuffers == Memory(4, SizeUnit.GB)) shouldBe true
                }
            }
            it("when osType == Windows then 512 MB is maximum of sharedBuffers") {
                val systemConfiguration = mockk<SystemConfiguration>(relaxed = true)
                every { systemConfiguration.dbApplication } returns DbApplication.DESKTOP
                every { systemConfiguration.osType } returns OperatingSystem.Windows
                every { systemConfiguration.ram } returns Memory(16, SizeUnit.GB)
                (SharedBuffers(systemConfiguration).sharedBuffers == Memory(512, SizeUnit.MB)) shouldBe true
            }
            it("when dbApplication == WEB, osType == Windows and ram == 1 GB then sharedBuffers should be 256MB ") {
                val systemConfiguration = mockk<SystemConfiguration>(relaxed = true)
                every { systemConfiguration.dbApplication } returns DbApplication.WEB
                every { systemConfiguration.osType } returns OperatingSystem.Windows
                every { systemConfiguration.ram } returns Memory(1, SizeUnit.GB)
                (SharedBuffers(systemConfiguration).sharedBuffers == Memory(256, SizeUnit.MB)) shouldBe true
            }
        }
    }

}