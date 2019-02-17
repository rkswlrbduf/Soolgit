package blackstone.com.soolgit.Util

import com.squareup.otto.Bus

class BusProvider {

    companion object {
        val BUS = Bus()
    }

    open fun getInstance(): Bus {
        return BUS
    }

    private fun BusProvider() {
        // No instances.
    }


}