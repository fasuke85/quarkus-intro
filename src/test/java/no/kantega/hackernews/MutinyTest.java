package no.kantega.hackernews;


import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

@Disabled
public class MutinyTest {

    /**
     * Fra https://smallrye.io/smallrye-mutiny/getting-started/creating-unis
     *
     * A Uni<T> is a specialized stream that emits only an item or a failure. Typically, Uni<T> are great to represent
     * asynchronous actions such as a remote procedure call, an HTTP request, or an operation producing a single result.
     * 
     */
    @Test
    public void uni() throws InterruptedException {
        // Vi lager Uni med Builder-syntax, og chainer sammen operasjoner
        var uni = Uni.createFrom().item("hello")
                // Vi kaller onItem for å gjøre det mulig å jobbe med det objektet som Uni wrapper (hello)
                .onItem()
                // vi kan når transformere hello til en ny Uni
                .transform(item -> item + " mutiny")
                .onItem()
                // transformerer en gang til
                .transform(String::toUpperCase)
                // for at noe som helst skal skje må vi kalle subscribe, med en callback til with-funksjonen
                .subscribe()
                .with(item -> System.out.println(item))
                ;



        var uniOther = Uni.createFrom().item("From the future")
                .onItem()
                .delayIt().by(Duration.ofMillis(1000))
                .map(String::toUpperCase)
                .onSubscription().invoke(s -> System.out.println("Someone has subscribed"))
                ;

        uniOther.subscribe().with(System.out::println);
        System.out.println("Has subscribed to uniOther");

        Thread.sleep(1100);
    }

    /**
     * Fra https://smallrye.io/smallrye-mutiny/getting-started/creating-multis
     *
     * A Multi<T> is a data stream that:
     *  - emits 0..n item events
     *  - emits a failure event
     *
     * Multi<T> provides many operators that create, transform, and orchestrate Multi sequences. The operators can be
     * used to define a processing pipeline. The events flow in this pipeline, and each operator can process or
     * transform the events.
     *
     * Multis are lazy by nature. To trigger the computation, you must subscribe.
     *
     */
    @Test
    public void multi() {
        // samme api-style som for Uni
        var multi = Multi.createFrom().items("foo", "bar", "baz")
                // Nå vi kaller onItem vil vi bare jobbe med ett item (feks foo) om gangen
                .onItem()
                .transform(String::toUpperCase)
                .subscribe()
                // callback kalles for hvert item
                .with(System.out::println);

        // Vi kan lage en Multi fra en List
        var multiFrom = Multi.createFrom().iterable(List.of(1L, 2L, 3L));

        // Vi kan lage en Multi fra en Uni<List<T>>
        var uniOfLongList = Uni.createFrom().item(List.of(1L, 2L, 3L));
        var multiOfLong = uniOfLongList.onItem()
                .transformToMulti(list -> Multi.createFrom().iterable(list))
                .onItem()
                .transform(item -> item * 2)
                .subscribe()
                .with(System.out::println)
                ;

    }

}
