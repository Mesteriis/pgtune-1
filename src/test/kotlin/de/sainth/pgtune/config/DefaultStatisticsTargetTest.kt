package de.sainth.pgtune.config

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.kotlintest.tables.row
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk

@MicronautTest
class DefaultStatisticsTargetTest() : DescribeSpec() {

    init {
        describe("DefaultStatisticsTargetTest") {
            val systemConfiguration = mockk<SystemConfiguration>(relaxed = true)
            forall(
                    row(DbApplication.WEB, 100),
                    row(DbApplication.DATA_WAREHOUSE, 500),
                    row(DbApplication.OLTP, 100),
                    row(DbApplication.DESKTOP, 100),
                    row(DbApplication.MIXED, 100)
            ) { app, expected ->
                every { systemConfiguration.dbApplication } returns app
                DefaultStatisticsTarget(systemConfiguration).defaultStatisticsTarget shouldBe expected
            }
        }
    }

}
