import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CoinChanger {

    private List<Coin> coins;

    CoinChanger(List<Integer> coins) {
        this.coins = sort(coins);
    }

    private List<Coin> sort(List<Integer> coins) {
        coins.sort(Comparator.naturalOrder());
        Collections.reverse(coins);
        return coins.stream()
                .map(Coin::new)
                .collect(Collectors.toList());
    }

    List<Integer> getChange(int amount) {
        List<Integer> change = new ArrayList<>();
        for (Coin coin : coins) {
            while (amount >= coin.getValue()) {
                amount -= coin.getValue();
                change.add(coin.getValue());
            }
        }
        return change;
    }

}
