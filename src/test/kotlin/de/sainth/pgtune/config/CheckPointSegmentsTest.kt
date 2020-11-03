package de.sainth.pgtune.config

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.DescribeSpec
import io.kotlintest.tables.row
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk

@MicronautTest
class CheckPointSegmentsTest() : DescribeSpec() {

    init {
        describe("CheckPointSegmentsTest") {
            it("when dbVersion != V9_4 then IllegalArgumentException is thrown") {
                val systemConfiguration = mockk<SystemConfiguration>(relaxed = true)
                val dbVersions = listOf(PostgresVersion.V9_5, PostgresVersion.V9_6, PostgresVersion.V10, PostgresVersion.V11, PostgresVersion.V12, PostgresVersion.V13)
                dbVersions.forEach { dbVersion ->
                    every { systemConfiguration.dbVersion } returns dbVersion
                    shouldThrow<IllegalArgumentException> {
                        CheckPointSegments(systemConfiguration)
                    }
                }
            }
        }
        describe("when dbVersion == V9_4 the correct static values are present") {
            val systemConfiguration = mockk<SystemConfiguration>(relaxed = true)
            every { systemConfiguration.dbVersion } returns PostgresVersion.V9_4
            forall(
                    row(DbApplication.WEB, 32),
                    row(DbApplication.OLTP, 64),
                    row(DbApplication.DATA_WAREHOUSE, 128),
                    row(DbApplication.DESKTOP, 3),
                    row(DbApplication.MIXED, 32)
            ) { app, segments ->
                every { systemConfiguration.dbApplication } returns app
                CheckPointSegments(systemConfiguration).checkPointSegments shouldBe segments
            }
        }
    }

}