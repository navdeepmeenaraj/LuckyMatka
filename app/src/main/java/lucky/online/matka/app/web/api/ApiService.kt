package lucky.online.matka.app.web.api

import lucky.online.matka.app.web.model.*
import lucky.online.matka.app.web.model.gali.BidResponse
import lucky.online.matka.app.web.model.gali.GaliMarkets
import lucky.online.matka.app.web.model.gali.bidhis
import lucky.online.matka.app.web.model.gali.date
import lucky.online.matka.app.web.model.login.AuthResponse
import lucky.online.matka.app.web.model.starline.*
import lucky.online.matka.app.utils.Constants.ADD_FUNDS
import lucky.online.matka.app.utils.Constants.AUTH_HEADER
import lucky.online.matka.app.utils.Constants.BANK_DETAILS
import lucky.online.matka.app.utils.Constants.GAME_RATES
import lucky.online.matka.app.utils.Constants.IMAGE_LINK
import lucky.online.matka.app.utils.Constants.PASSWORD_RESET
import lucky.online.matka.app.utils.Constants.PAYMENT_DETAILS
import lucky.online.matka.app.utils.Constants.PAYMENT_NUMBER
import lucky.online.matka.app.utils.Constants.PAYMENT_TYPE
import lucky.online.matka.app.utils.Constants.PLACE_BID
import lucky.online.matka.app.utils.Constants.RETRIEVE_PAYMENT_DETAILS
import lucky.online.matka.app.utils.Constants.TRANSFER_POINTS
import lucky.online.matka.app.utils.Constants.USER_ID
import lucky.online.matka.app.utils.Constants.VIDEO_LINK
import lucky.online.matka.app.utils.Constants.WITHDRAW_FUNDS
import lucky.online.matka.app.utils.Constants.WITHDRAW_REQUEST_HISTORY
import lucky.online.matka.app.utils.NetworkEndpoints.APP_CONFIG_ENDPOINT
import lucky.online.matka.app.utils.NetworkEndpoints.BANNER_IMAGE_ENDPOINT
import lucky.online.matka.app.utils.NetworkEndpoints.BET_HISTORY_ENDPOINT
import lucky.online.matka.app.utils.NetworkEndpoints.BET_PLACE_ENDPOINT
import lucky.online.matka.app.utils.NetworkEndpoints.CHART
import lucky.online.matka.app.utils.NetworkEndpoints.CURRENT_DATE_ENDPOINT
import lucky.online.matka.app.utils.NetworkEndpoints.DATE
import lucky.online.matka.app.utils.NetworkEndpoints.GALI_BIDS
import lucky.online.matka.app.utils.NetworkEndpoints.GALI_MARKETS
import lucky.online.matka.app.utils.NetworkEndpoints.GALI_WINS
import lucky.online.matka.app.utils.NetworkEndpoints.IS_USER_VERIFIED
import lucky.online.matka.app.utils.NetworkEndpoints.LOGIN_ENDPOINT
import lucky.online.matka.app.utils.NetworkEndpoints.MARKET_ENDPOINT
import lucky.online.matka.app.utils.NetworkEndpoints.NAME_FIELD
import lucky.online.matka.app.utils.NetworkEndpoints.ONE_MARKET_DATA_ENDPOINT
import lucky.online.matka.app.utils.NetworkEndpoints.PASSCODE
import lucky.online.matka.app.utils.NetworkEndpoints.PASSWORD_FIELD
import lucky.online.matka.app.utils.NetworkEndpoints.REGISTER_ENDPOINT
import lucky.online.matka.app.utils.NetworkEndpoints.SERVER_CHECK_ENDPOINT
import lucky.online.matka.app.utils.NetworkEndpoints.STARLINE_BIDS
import lucky.online.matka.app.utils.NetworkEndpoints.STARLINE_MARKETS_ENDPOINT
import lucky.online.matka.app.utils.NetworkEndpoints.STARLINE_PLACE_BID
import lucky.online.matka.app.utils.NetworkEndpoints.STARLINE_RATES_ENDPOINT
import lucky.online.matka.app.utils.NetworkEndpoints.STARLINE_WINS
import lucky.online.matka.app.utils.NetworkEndpoints.TRANS_ENDPOINT
import lucky.online.matka.app.utils.NetworkEndpoints.USERNAME_FIELD
import lucky.online.matka.app.utils.NetworkEndpoints.USER_PROFILE_ENDPOINT
import lucky.online.matka.app.utils.NetworkEndpoints.WIN_HISTORY_ENDPOINT
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST(LOGIN_ENDPOINT)
    suspend fun provideLoginService(
        @Field(USERNAME_FIELD) username: String,
        @Field(PASSWORD_FIELD) password: String
    ): Response<AuthResponse>

    @FormUrlEncoded
    @POST(REGISTER_ENDPOINT)
    suspend fun provideRegisterService(
        @Field(NAME_FIELD) name: String,
        @Field(USERNAME_FIELD) username: String,
        @Field(PASSWORD_FIELD) password: String,
        @Field(PASSCODE) pin: String
    ): Response<RegisterResponse>

    @GET(MARKET_ENDPOINT)
    suspend fun provideMarketData(
        @Header(AUTH_HEADER) token: String
    ): Response<ArrayList<MarketData>>


    @FormUrlEncoded
    @POST(ONE_MARKET_DATA_ENDPOINT)
    suspend fun provideOneMarketData(
        @Header(AUTH_HEADER) token: String,
        @Field("market_id") mainMarketId: Int
    ): Response<SingleMarketData>

    @FormUrlEncoded
    @POST("user_points")
    suspend fun provideUserPoints(
        @Header(AUTH_HEADER) token: String,
        @Field("user_id") userId: Int
    ): Response<UserPoints>

    @GET(SERVER_CHECK_ENDPOINT)
    suspend fun provideServerStatus(
        @Header(AUTH_HEADER) token: String
    ): Response<ServerCheck>

    @FormUrlEncoded
    @POST(BET_PLACE_ENDPOINT)
    suspend fun provideBetPlace(
        @FieldMap map: HashMap<String, Any?>
    ): Response<PlaceBet>


    @FormUrlEncoded
    @POST(USER_PROFILE_ENDPOINT)
    suspend fun provideUserProfile(
        @Header(AUTH_HEADER) token: String,
        @Field("id") userId: Int
    ): Response<UserProfile>

    @GET(BANNER_IMAGE_ENDPOINT)
    suspend fun provideBannerImage(
        @Header(AUTH_HEADER) token: String
    ): Response<BannerImage>

    @FormUrlEncoded
    @POST(BET_HISTORY_ENDPOINT)
    suspend fun provideBetHistory(
        @Header(AUTH_HEADER) token: String,
        @Field("user_id") userId: Int,
        @Field("from") from: String,
        @Field("to") to: String
    ): Response<List<BidHistory>>

    @FormUrlEncoded
    @POST(WIN_HISTORY_ENDPOINT)
    suspend fun provideWinHistory(
        @Header(AUTH_HEADER) token: String,
        @Field("user_id") userId: Int,
        @Field("from") from: String,
        @Field("to") to: String
    ): Response<List<WinHistory>>

    @GET(CURRENT_DATE_ENDPOINT)
    suspend fun provideCurrentDate(): Response<String>

    @GET(APP_CONFIG_ENDPOINT)
    suspend fun provideAppConfig(
        @Header(AUTH_HEADER) token: String
    ): Response<AppConfig>

    @GET(STARLINE_MARKETS_ENDPOINT)
    suspend fun provideStarMarkets(
        @Header(AUTH_HEADER) token: String
    ): Response<List<StarlineMarkets>>

    @GET(STARLINE_RATES_ENDPOINT)
    suspend fun provideStarRates(
        @Header(AUTH_HEADER) token: String
    ): Response<StarlineRates>

    @FormUrlEncoded
    @POST(STARLINE_PLACE_BID)
    suspend fun provideStarBidPlace(
        @Header(AUTH_HEADER) token: String,
        @FieldMap map: HashMap<String, Any?>
    ): Response<StarBidPlace>

    @FormUrlEncoded
    @POST(STARLINE_WINS)
    suspend fun provideStarWinHistory(
        @Header(AUTH_HEADER) token: String,
        @Field("user_id") userID: Int
    ): Response<List<StarWins>>

    @FormUrlEncoded
    @POST(STARLINE_BIDS)
    suspend fun provideStarBidHistory(
        @Header(AUTH_HEADER) token: String,
        @Field("user_id") userID: Int
    ): Response<List<StarlineBids>>

    @GET(GALI_MARKETS)
    suspend fun provideGaliMarkets(): Response<List<GaliMarkets>>

    @GET(DATE)
    suspend fun getDate(): Response<date>

    @FormUrlEncoded
    @POST(PLACE_BID)
    suspend fun placeGaliBid(
        @Header(AUTH_HEADER) token: String,
        @FieldMap map: HashMap<String, Any?>
    ): Response<BidResponse>

    @FormUrlEncoded
    @POST(GALI_WINS)
    suspend fun provideGaliWinHistory(
        @Header(AUTH_HEADER) token: String,
        @Field("user_id") userID: Int
    ): Response<List<bidhis>>

    @FormUrlEncoded
    @POST(GALI_BIDS)
    suspend fun provideGaliBidHistory(
        @Header(AUTH_HEADER) token: String,
        @Field("user_id") userID: Int
    ): Response<List<bidhis>>


    @FormUrlEncoded
    @POST(CHART)
    suspend fun provideMarketChart(
        @Field("id") id: Int
    ): Response<Charts>

    @FormUrlEncoded
    @POST(TRANS_ENDPOINT)
    suspend fun getTransactionHistory(
        @Header(AUTH_HEADER) token: String,
        @Field("id") id: Int,
        @Field("from") from: String,
        @Field("to") to: String
    ): Response<List<WalletTransaction>>

    @GET(PAYMENT_DETAILS)
    suspend fun getPaymentDetails(
        @Header(AUTH_HEADER) token: String
    ): Response<PaymentDetails>

    @POST(ADD_FUNDS)
    suspend fun addFunds(
        @Header(AUTH_HEADER) token: String,
        @Body request: DepositModel
    ): Response<DepositResponse>

    @POST(WITHDRAW_FUNDS)
    suspend fun withdrawFunds(
        @Header(AUTH_HEADER) token: String,
        @Body request: WithdrawRequest
    ): Response<WithdrawResponse>

    @POST(PASSWORD_RESET)
    suspend fun resetPassword(
        @Body request: PasswordReset
    ): Response<PasswordResetResponse>

    @FormUrlEncoded
    @POST(IS_USER_VERIFIED)
    suspend fun checkUserStatus(
        @Header(AUTH_HEADER) token: String,
        @Field("user_id") userID: Int
    ): Response<UserVerified>

    @FormUrlEncoded
    @POST(WITHDRAW_REQUEST_HISTORY)
    suspend fun fetchWithdrawRequestHistory(
        @Header(AUTH_HEADER) token: String,
        @Field("user_id") userID: Int
    ): Response<List<WithdrawList>>

    @GET(GAME_RATES)
    suspend fun fetchGameRates(): Response<List<MarketRates>>

    @FormUrlEncoded
    @POST(BANK_DETAILS)
    suspend fun addPaymentDetails(
        @Header(AUTH_HEADER) token: String,
        @Field(USER_ID) userID: Int,
        @Field(PAYMENT_TYPE) paymentType: Int,
        @Field(PAYMENT_NUMBER) paymentNumber: String
    ): Response<PaymentUpdateResponse>

    @FormUrlEncoded
    @POST(RETRIEVE_PAYMENT_DETAILS)
    suspend fun fetchPaymentDetails(
        @Header(AUTH_HEADER) token: String,
        @Field(USER_ID) userID: Int
    ): Response<UserBankDetails>


    @GET(VIDEO_LINK)
    suspend fun getVideoLink(
        @Header(AUTH_HEADER) token: String
    ): Response<VideoLink>


    @GET(IMAGE_LINK)
    suspend fun getBannerImageList(): Response<BannerImageList>


    @FormUrlEncoded
    @POST(TRANSFER_POINTS)
    suspend fun transferPoints(
        @Header(AUTH_HEADER) token: String,
        @Field(USER_ID) userID: Int,
        @Field("transfer_mobile") mobile: String,
        @Field("transfer_amount") amount: String
    ): Response<TransferPointsResponse>

    @FormUrlEncoded
    @POST("verify_user")
    suspend fun verifyUser(
        @Header(AUTH_HEADER) token: String,
        @Field(USER_ID) userID: Int,
        @Field("transfer_mobile") mobile: String,
    ): Response<VerifyUser>

}