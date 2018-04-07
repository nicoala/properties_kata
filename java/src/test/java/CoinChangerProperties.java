import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

@RunWith(JUnitQuickcheck.class)
public class CoinChangerProperties {

    private static final List<Integer> EURO_COINS = Arrays.asList(1, 2, 5, 10, 20, 50, 100, 200);

    private CoinChanger coinChanger;

    @Before
    public void setUp() {
        coinChanger = new CoinChanger(EURO_COINS);
    }

    @Property
    public void sum_of_coins_is_equal_to_original_amount(@InRange(minInt = 0, maxInt = 1000) int amount) {
        assumeThat(amount).isGreaterThanOrEqualTo(0).isLessThanOrEqualTo(1000);

        List<Integer> change = coinChanger.getChange(amount);

        int sum = change.stream()
                .mapToInt(Integer::intValue)
                .sum();

        assertThat(sum).isEqualTo(amount);
    }

    @Property
    public void should_only_return_change_with_existing_coins(@InRange(minInt = 0, maxInt = 1000) int amount) {
        List<Integer> change = coinChanger.getChange(amount);

        assertThat(change).allMatch(EURO_COINS::contains);
    }

    @Property
    public void should_return_a_single_coin_for_an_amout_equal_to_one_of_them(@From(SingleCoinGenerator.class) int amount) {
        List<Integer> change = coinChanger.getChange(amount);

        assertThat(change).hasSize(1);
    }

    @Property
    public void sum_of_coins_smaller_than_a_given_coin_should_be_smaller_than_this_coin_value(
            @InRange(minInt = 0, maxInt = 1000) int amount,
            @From(SingleCoinGenerator.class) int givenCoin) {

        List<Integer> change = coinChanger.getChange(amount);

        int sum = change.stream()
                .mapToInt(Integer::intValue)
                .filter(coin -> coin < givenCoin)
                .sum();

        assertThat(sum).isLessThan(givenCoin);
    }

    public static class SingleCoinGenerator extends Generator<Integer> {

        public SingleCoinGenerator() {
            super(Arrays.asList(Integer.class, int.class));
        }

        @Override
        public Integer generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
            return EURO_COINS.get(sourceOfRandomness.nextInt(EURO_COINS.size()));
        }
    }
}