package de.sainth.pgtune.config

class DefaultStatisticsTarget(systemConfiguration: SystemConfiguration) : PgConfigurationParameter("default_statistics_target") {
    val defaultStatisticsTarget: Int = when(systemConfiguration.dbApplication) {
        DbApplication.DATA_WAREHOUSE -> 500
        else -> 100
    }

    override fun getParameterString(): String {
        return "$defaultStatisticsTarget"
    }
}
