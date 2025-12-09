package com.taras.pet.crytra

import app.cash.turbine.test
import com.taras.pet.crytra.core.domain.util.NetworkError
import com.taras.pet.crytra.core.domain.util.Result
import com.taras.pet.crytra.crypto.domain.Coin
import com.taras.pet.crytra.crypto.domain.CoinPrice
import com.taras.pet.crytra.crypto.presentation.CoinUi
import com.taras.pet.crytra.crypto.presentation.DisplayableNumber
import com.taras.pet.crytra.crypto.presentation.coin_list.CoinListAction
import com.taras.pet.crytra.crypto.presentation.coin_list.CoinListEvent
import com.taras.pet.crytra.crypto.presentation.coin_list.CoinListViewModel
import com.taras.pet.crytra.crypto.presentation.toDisplayableNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.ZonedDateTime

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class CoinListViewModelTest {

    private lateinit var viewModel: CoinListViewModel
    private lateinit var fakeCoinDataSource: FakeCoinDataSource
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeCoinDataSource = FakeCoinDataSource()
        viewModel = CoinListViewModel(fakeCoinDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should load coins automatically`() = runTest {
        // Given
        val mockCoins = listOf(
            createMockCoin("btc", "Bitcoin"),
            createMockCoin("eth", "Ethereum")
        )
        fakeCoinDataSource.coinsToReturn = Result.Success(mockCoins)

        // When
        viewModel.state.test {
            val initialState = awaitItem()
            assertTrue(initialState.isInitialLoading)
            assertTrue(initialState.coins.isEmpty())

            advanceUntilIdle()

            val loadedState = awaitItem()
            assertFalse(loadedState.isLoading)
            assertFalse(loadedState.isInitialLoading)
            assertEquals(2, loadedState.coins.size)
            assertEquals("btc", loadedState.coins[0].id)
            assertEquals("eth", loadedState.coins[1].id)
        }
    }
    
    @Test
    fun `onAction OnCoinClick should select coin and load history`() = runTest {
        // Given
        val coin = createMockCoinUi("btc", "Bitcoin")
        val mockHistory = listOf(
            CoinPrice(
                priceUsd = 50000.0,
                dateTime = ZonedDateTime.now().minusHours(6)
            ),
            CoinPrice(
                priceUsd = 51000.0,
                dateTime = ZonedDateTime.now()
            )
        )
        fakeCoinDataSource.coinsToReturn = Result.Success(emptyList())
        fakeCoinDataSource.historyToReturn = Result.Success(mockHistory)

        // When
        viewModel.state.test {
            // Skip initial state and wait for coins to load
            awaitItem() // initial state with isInitialLoading = true
            advanceUntilIdle()
            awaitItem() // coins loaded state

            viewModel.onAction(CoinListAction.OnCoinClick(coin))

            // Then
            val selectedState = awaitItem()
            assertNotNull(selectedState.selectedCoin)
            assertEquals("btc", selectedState.selectedCoin?.id)

            advanceUntilIdle()

            val historyLoadedState = awaitItem()
            assertNotNull(historyLoadedState.selectedCoin?.coinPriceHistory)
            assertEquals(2, historyLoadedState.selectedCoin?.coinPriceHistory?.size)
        }
    }

    @Test
    fun `loading coins with error should emit error event`() = runTest {
        // Given
        fakeCoinDataSource.coinsToReturn = Result.Error(NetworkError.REQUEST_TIMEOUT)

        // When
        viewModel.events.test {
            viewModel.state.test {
                skipItems(1) // Skip initial state
                advanceUntilIdle()

                val finalState = awaitItem()
                assertFalse(finalState.isLoading)
                assertFalse(finalState.isInitialLoading)
                assertTrue(finalState.coins.isEmpty())
            }

            // Then
            val event = awaitItem()
            assertTrue(event is CoinListEvent.Error)
            assertEquals(NetworkError.REQUEST_TIMEOUT, (event as CoinListEvent.Error).error)
        }
    }

    @Test
    fun `loading coin history with error should emit error event`() = runTest {
        // Given
        val coin = createMockCoinUi("btc", "Bitcoin")
        fakeCoinDataSource.coinsToReturn = Result.Success(emptyList())
        fakeCoinDataSource.historyToReturn = Result.Error(NetworkError.NO_INTERNET)

        // When
        viewModel.events.test {
            viewModel.onAction(CoinListAction.OnCoinClick(coin))
            advanceUntilIdle()

            // Then
            val event = awaitItem()
            assertTrue(event is CoinListEvent.Error)
            assertEquals(NetworkError.NO_INTERNET, (event as CoinListEvent.Error).error)
        }
    }

    @Test
    fun `selected coin should be updated in state on click`() = runTest {
        // Given
        val coin = createMockCoinUi("btc", "Bitcoin")
        fakeCoinDataSource.historyToReturn = Result.Success(emptyList())

        // When
        viewModel.state.test {
            skipItems(1)
            advanceUntilIdle()

            assertNull(awaitItem().selectedCoin)

            viewModel.onAction(CoinListAction.OnCoinClick(coin))
            advanceUntilIdle()

            // Then
            val state = awaitItem()
            assertNotNull(state.selectedCoin)
            assertEquals("btc", state.selectedCoin?.id)
            assertEquals("Bitcoin", state.selectedCoin?.name)
        }
    }

    @Test
    fun `isInitialLoading should be false after first load`() = runTest {
        // Given
        fakeCoinDataSource.coinsToReturn = Result.Success(emptyList())

        // When
        viewModel.state.test {
            val initialState = awaitItem()
            assertTrue(initialState.isInitialLoading)

            advanceUntilIdle()

            val loadedState = awaitItem()
            assertFalse(loadedState.isInitialLoading)
        }
    }
    private fun createMockCoin(id: String, name: String) = Coin(
        id = id,
        rank = 1,
        name = name,
        symbol = id.uppercase(),
        marketCapUsd = 1000000.0,
        priceUsd = 50000.0,
        percentChange24h = 2.5
    )
    private fun createMockCoinUi(id: String, name: String) = CoinUi(

        id = id,
        rank = 1,
        name = name,
        symbol = id.uppercase(),
        priceUsd = 50.000.toDisplayableNumber(),
        marketCapUsd = 1000000.0.toDisplayableNumber(),
        percentChange24h = 2.5.toDisplayableNumber(),
        coinPriceHistory = emptyList(),
        iconRes = 0
    )
}