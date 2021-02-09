package com.airongomes.taskmanager

/**
 * Interface to define DrawerLockMode
 */
interface DrawerLockMode {

    /**
     * Define if drawer swiping gesture is locked or enabled
     */
    fun setDrawerLocked(enabled: Boolean)
}