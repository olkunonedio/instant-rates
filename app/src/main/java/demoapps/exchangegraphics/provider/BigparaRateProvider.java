package demoapps.exchangegraphics.provider;

import java.util.List;

import demoapps.exchangegraphics.data.BuySellRate;
import demoapps.exchangegraphics.service.Api;
import demoapps.exchangegraphics.service.BigparaService;
import demoapps.exchangegraphics.service.EnparaService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by erdemmac on 25/11/2016.
 */

public class BigparaRateProvider extends PoolingDataProvider<List<BuySellRate>> implements IRateProvider {

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            fetch();
        }
    };

    public BigparaRateProvider(Callback callback) {
        super(callback);
    }

    private void fetch() {
        final BigparaService bigparaService = Api.getBigparaApi().create(BigparaService.class);
        Call<List<BuySellRate>> call = bigparaService.getData();
        call.enqueue(new retrofit2.Callback<List<BuySellRate>>() {
            @Override
            public void onResponse(Call<List<BuySellRate>> call, Response<List<BuySellRate>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BuySellRate> rates = response.body();
                    notifyValue(rates);
                    fetchAgain(false);
                } else {
                    fetchAgain(true);
                    notifyError();
                }
            }

            @Override
            public void onFailure(Call<List<BuySellRate>> call, Throwable t) {
                fetchAgain(true);
                notifyError();
            }
        });
    }

    @Override
    Runnable getWork() {
        return runnable;
    }
}
