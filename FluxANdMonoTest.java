package com.kris.first;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.junit.Test;

import javafx.scene.ParallelCamera;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;

public class FluxANdMonoTest {
	@Test
	public void fluxTest() {
		System.out.println("\n************************* - 1) Normal stringFlux \n");
		Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring");
		stringFlux.subscribe(System.out::println);

		System.out.println("\n************************* - 2)stringFluxWithError \n");
		Flux<String> stringFluxWithError = Flux.just("Spring", "Spring Boot", "Reactive Spring")
				.concatWith(Flux.error(new RuntimeException("New Run Time Exception Occured")));
		stringFluxWithError.subscribe(System.out::println, (e) -> System.err.println("SYSOUT Exception is " + e));

		System.out.println("\n************************* - 3) stringFluxWithErrorWithLog \n");
		Flux<String> stringFluxWithErrorWithLog = Flux.just("Spring", "Spring Boot", "Reactive Spring")
				.concatWith(Flux.error(new RuntimeException("New Run Time Exception Occured")))
				.concatWith(Flux.just("After Error")).log();
		stringFluxWithErrorWithLog.subscribe(System.out::println, (e) -> System.err.println("SYSOUT Exception is " + e),
				() -> System.out.println("SYSOUT Completed"));

		System.out.println("\n************************* - 4) stringFluxWithoutErrorWithLog \n");
		Flux<String> stringFluxWithoutErrorWithLog = Flux.just("Spring", "Spring Boot", "Reactive Spring").log();
		stringFluxWithoutErrorWithLog.subscribe(System.out::println,
				(e) -> System.err.println("SYSOUT Exception is " + e), () -> System.out.println("SYSOUT Completed"));

//		Factory Methods

		System.out.println("\n************************* - 5) From List using Iterable \n");
		List<String> namesFlux1 = Arrays.asList("name1", "name2", "name3", "name4");
		Flux<String> stringFluxFromList = Flux.fromIterable(namesFlux1);
		stringFluxFromList.subscribe(System.out::println);

		System.out.println("\n************************* - 6) From Array  \n");
		String[] namesFlux2 = new String[] { "name1", "name2", "name3", "name4" };
		Flux<String> stringFluxFromArray = Flux.fromArray(namesFlux2);
		stringFluxFromArray.subscribe(System.out::println);

		System.out.println("\n************************* - 7) From Streams  \n");
		List<String> namesFlux3 = Arrays.asList("name1", "name2", "name3", "name4");
		Flux<String> stringFluxFromStreams = Flux.fromStream(namesFlux3.stream());
		stringFluxFromStreams.subscribe(System.out::println);

		System.out.println("\n************************* - 8) Range FLux  \n");
		Flux<Integer> rangeFlux = Flux.range(1, 5);
		rangeFlux.subscribe(System.out::println);

//		Filtering a Reactive Stream

		System.out.println("\n************************* - 9) Filter \n");
		List<String> namesList1 = Arrays.asList("name1", "name2", "aname3", "aname4");
		Flux<String> namesFluxFilterList = Flux.fromIterable(namesList1).filter(s -> s.startsWith("n"));
		namesFluxFilterList.subscribe(System.out::println);

//     Transforming a Reactive Stream using Map

		System.out.println("\n************************* - 11) Transforming Reactive STream Using Map \n");
		List<String> namesList2 = Arrays.asList("name1", "name2", "aname3", "aname4");
		Flux<String> namesFluxMapFLux2 = Flux.fromIterable(namesList2).map(s -> s.toUpperCase());
		namesFluxMapFLux2.subscribe(System.out::println);

		System.out.println("\n************************* - 12) Transforming Reactive STream Using Map \n");
		List<String> namesList3 = Arrays.asList("name1", "name2", "aname3", "aname4");
		Flux<Integer> namesFluxMapFLux3 = Flux.fromIterable(namesList3).map(s -> s.length());
		namesFluxMapFLux3.subscribe(System.out::println);

//		Repeat the FLux
		System.out.println("\n************************* - 12) Transforming Reactive STream Using Map \n");
		List<String> namesList4 = Arrays.asList("n1", "na2", "anae3", "aname4");
		Flux<Integer> namesFluxMapFLux4 = Flux.fromIterable(namesList4).map(s -> s.length()).repeat(1);
		namesFluxMapFLux4.subscribe(System.out::println);
		
//		FIlter, Map the FLux (PIPELINE)
				System.out.println("\n************************* - 13) Transforming Reactive STream Using Map \n");
				List<String> namesList5 = Arrays.asList("name1", "name2", "aname3", "aname4");
				Flux<String> namesFluxMapFLux5 = Flux.fromIterable(namesList5).filter(s->s.length()>5).map(s -> s.toUpperCase());
				namesFluxMapFLux5.subscribe(System.out::println);
				
				//Map used for transforming doing OPERATIONS ON ELEMENTS/OBJECTS IN A FLUX,(eg update an element in flux)
				//FLATMAP used for doing OPERATIONS ON FLUX itself(saving the updated flux)
//		Transforming a Reactive Stream using FlatMap (for DB Call or External Service Call that returns a flux)
				System.out.println("\n************************* - 14) Transforming Reactive STream Using Map \n");
				Flux<String> namesFluxMapFLux6 = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F")).flatMap(s-> {
					return Flux.fromIterable(convertToList(s));
				});
				namesFluxMapFLux6.subscribe(System.out::println);
				
//				For faster Operation when compared to prev case use Parallel - Vdo 19
//				SubsriobeOn is used to make the switchover of threads
//						System.out.println("\n************************* - 15) Transforming Reactive STream Using Map \n");
//						Flux<String> namesFluxMapFLux6 = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
//								.window(2)
//								.flatMap((s)->
//								s.map(this::convertToList).subscribeOn(parallel()))
//								.flatMap(s->  Flux.fromIterable(convertToList(s)));
//						namesFluxMapFLux6.subscribe(System.out::println);
				
//				Using COncatMap - Same as FlatMap But Maintains the Order. DIsadv: Takes Time like Non Parallel- Vdo 19
//				System.out.println("\n************************* - 16) Transforming Reactive STream Using Map \n");
//				Flux<String> namesFluxMapFLux6 = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
//						.window(2)
//						.flatMap((s)->
//						s.map(this::convertToList).subscribeOn(parallel()))
//						.flatMap(s->  Flux.fromIterable(convertToList(s)));
//				namesFluxMapFLux6.subscribe(System.out::println);

//				Using FLatMapSequential - Maintains Order and it is faster .Hence Disadv of ConcatMap is fixed here
//				System.out.println("\n************************* - 17) Transforming Reactive STream Using Map \n");
//				Flux<String> namesFluxMapFLux6 = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
//						.window(2)
//						.flatMapSequential((s)->
//						s.map(this::convertToList).subscribeOn(parallel()))
//						.flatMap(s->  Flux.fromIterable(convertToList(s)));
//				namesFluxMapFLux6.subscribe(System.out::println);
				
//				Merge FLux - Order is not maintained
				System.out.println("\n************************* - 18) Merge Flux \n");
				Flux<String> stringFlux1 = Flux.just("A", "B", "C");
				Flux<String> stringFlux2 = Flux.just("D", "E", "F");
				Flux<String> mergeFlux = Flux.merge(stringFlux1, stringFlux2);
				mergeFlux.subscribe(System.out::println);
				
//				Merge FLux - with Delay    (O/p will not be visible here...O/P comes only with StepVerifier if there is Delay
				System.out.println("\n************************* - 19) Merge Flux \n");
				Flux<String> stringFluxWithDelay1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1)).log();
				Flux<String> stringFluxWithDelay2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1)).log();
				Flux<String> mergeFluxWithDelay = Flux.merge(stringFluxWithDelay1, stringFluxWithDelay2).log();
				mergeFluxWithDelay.subscribe(System.out::println);
				
//				Concat FLux - with Order even if there is delay
				System.out.println("\n************************* - 20) Concat Flux \n");
				Flux<String> stringFluxWithOrder1 = Flux.just("A", "B", "C");
				Flux<String> stringFluxWithOrder2 = Flux.just("D", "E", "F");
				Flux<String> mergeFluxWithOrder = Flux.concat(stringFluxWithOrder1, stringFluxWithOrder2);
				mergeFluxWithOrder.subscribe(System.out::println);
				
//				Zip FLux - with Order even if there is delay
				System.out.println("\n************************* - 21) Zip Flux \n");
				Flux<String> stringFluxZip1 = Flux.just("A", "B", "C");
				Flux<String> stringFluxZip2 = Flux.just("D", "E", "F");
				Flux<String> mergeFluxZip = Flux.zip(stringFluxZip1, stringFluxZip2, (t1,t2)->{
					return t1.concat(t2);
				});
				mergeFluxZip.subscribe(System.out::println);
				
//				Error Handling Approach 1 - Rteurn a Default Flux
				System.out.println("\n************************* - 22)Error Handling  using onErrorResume \n");
				Flux<String> stringFluxWithError1 = Flux.just("Spring", "Spring Boot", "Reactive Spring")
						.concatWith(Flux.error(new RuntimeException("New Run Time Exception Occured"))).concatWith(Flux.just("After Error")).onErrorResume((e)->{
							System.out.println("Sysout Error Handled :" + e);
							return Flux.just("default1", "Default2");
						});
				stringFluxWithError1.subscribe(System.out::println, (e) -> System.err.println("SYSOUT Exception is " + e));

//				Error Handling Approach 2 - Return a String
				System.out.println("\n************************* - 23)Error Handling using onErrorReturn \n");
				Flux<String> stringFluxWithError2 = Flux.just("Spring", "Spring Boot", "Reactive Spring")
						.concatWith(Flux.error(new RuntimeException("New Run Time Exception Occured"))).concatWith(Flux.just("After Error")).onErrorReturn("OnReturn");
				stringFluxWithError2.subscribe(System.out::println, (e) -> System.err.println("SYSOUT Exception is " + e));

//				Error Handling Approach 3 - Return Exception from one type to another Type
				System.out.println("\n************************* - 24)Error Handling using onErrorMap \n");
				Flux<String> stringFluxWithError3 = Flux.just("Spring", "Spring Boot", "Reactive Spring")
						.concatWith(Flux.error(new RuntimeException("New Run Time Exception Occured"))).concatWith(Flux.just("After Error")).onErrorMap((e)->new MyCustomException(e));
				stringFluxWithError3.subscribe(System.out::println, (e) -> System.err.println("SYSOUT Exception is " + e));

//				Error Handling Approach 4 - Do a Retry for case of DB call like scenarios
				System.out.println("\n************************* - 25)Error Handling using Retry \n");
				Flux<String> stringFluxWithError4 = Flux.just("Spring", "Spring Boot", "Reactive Spring")
						.concatWith(Flux.error(new RuntimeException("New Run Time Exception Occured"))).concatWith(Flux.just("After Error")).onErrorMap((e)->new MyCustomException(e)).retry(2);
				stringFluxWithError4.subscribe(System.out::println, (e) -> System.err.println("SYSOUT Exception is " + e));

//				Error Handling Approach 4 - Do a Retry With Backoff - Try after some mentioned duration. But the exception will be IllegalstateException if it fails after retry also
//				- Not Working since No such method
//				System.out.println("\n************************* - 26)Error Handling using Retry \n");
//				Flux<String> stringFluxWithError5 = Flux.just("Spring", "Spring Boot", "Reactive Spring")
//						.concatWith(Flux.error(new RuntimeException("New Run Time Exception Occured"))).concatWith(Flux.just("After Error")).onErrorMap((e)->new MyCustomException(e)).retryBackoff(2, Duration.ofSeconds(5));
//				stringFluxWithError5.subscribe(System.out::println, (e) -> System.err.println("SYSOUT Exception is " + e));

//				Infinite Sequence
				System.out.println("\n************************* - 27)Infinite Sequence \n");
				Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(100)).log();
				infiniteFlux.subscribe((element)->System.out.println("Value is : "+element));
//				If this Thread.sleep is not called, then no value will be emitted as is Asynchronous and Non Blocking and hence when ithe main thread is closed it din;t print as the values were generated in Parallel thread);
//				try {
//					Thread.sleep(3000);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}

				
	}
	@Test
	public void BackPressureTest() {
		System.out.println("\n************************* - Request \n");
		Flux<Integer> finiteFLux = Flux.range(1, 10);
		finiteFLux.subscribe((element)->System.out.println("Element is : " + element), 
				(e)->System.err.println("Error is "+e),
				()->System.out.println("Done"),
				(subscription->subscription.request(2)));
		
		
		//FOr Cancel
		System.out.println("\n************************* - Cancel \n");
		System.out.println();
		Flux<Integer> finiteFLux1 = Flux.range(1, 10).log();
		finiteFLux1.subscribe((element)->System.out.println("Element is : " + element), 
				(e)->System.err.println("Error is "+e),
				()->System.out.println("Done"),
				(subscription->subscription.cancel()));
		
		//Customized BackPressure
		System.out.println("\n************************* - Customized BackPressure by using Hook \n");
		Flux<Integer> finiteFLux2 = Flux.range(1, 10).log();
		finiteFLux2.subscribe(new BaseSubscriber<Integer>() {
			@Override
			protected void hookOnNext(Integer value) {
				request(1);
				System.out.println("Values received via hook is : "+value);
				if(value==4) {
					cancel();
				}
			}
		});
		
	}

	private List<String> convertToList(String s) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Arrays.asList(s, "newValue");
	}

	
	@Test
	public void myTest() {
		System.out.println("\n************************* - 20) Merge Flux \n");
		Flux<String> stringFluxWithOrder1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
		Flux<String> stringFluxWithOrder2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));
		Flux<String> mergeFluxWithOrder = Flux.concat(stringFluxWithOrder1, stringFluxWithOrder2);
		mergeFluxWithOrder.subscribe(System.out::println);
}
	@Test
	public void monoTest() {
		System.out.println("\n************************* - 1) Normal stringMono \n");
		Mono<String> mono1 = Mono.just("Spring");
		mono1.subscribe(System.out::println);

//		Factory methods
		System.out.println("\n************************* - 2) Empty stringMono \n");
		Mono<String> mono2 = Mono.justOrEmpty(null);
		mono2.subscribe(System.out::println);

		System.out.println("\n************************* - 3) From Supplier \n");
		Supplier<String> stringSupplier = () -> "name1";
//		System.out.println(stringSupplier.get());
		Mono<String> mono3 = Mono.fromSupplier(stringSupplier);
		mono3.subscribe(System.out::println);

//		System.out.println("\n************************* - 2)stringFluxWithError \n");
//		Flux<String> stringFluxWithError = Flux.just("Spring", "Spring Boot", "Reactive Spring")
//				.concatWith(Flux.error(new RuntimeException("New Run Time Exception Occured")));
//		stringFluxWithError.subscribe(System.out::println, (e) -> System.err.println("SYSOUT Exception is " + e));
//
//		System.out.println("\n************************* - 3) stringFluxWithErrorWithLog \n");
//		Flux<String> stringFluxWithErrorWithLog = Flux.just("Spring", "Spring Boot", "Reactive Spring")
//				.concatWith(Flux.error(new RuntimeException("New Run Time Exception Occured")))
//				.concatWith(Flux.just("After Error")).log();
//		stringFluxWithErrorWithLog.subscribe(System.out::println, (e) -> System.err.println("SYSOUT Exception is " + e),
//				() -> System.out.println("SYSOUT Completed"));
//
//		System.out.println("\n************************* - 4) stringFluxWithoutErrorWithLog \n");
//		Flux<String> stringFluxWithoutErrorWithLog = Flux.just("Spring", "Spring Boot", "Reactive Spring").log();
//		stringFluxWithoutErrorWithLog.subscribe(System.out::println,
//				(e) -> System.err.println("SYSOUT Exception is " + e), () -> System.out.println("SYSOUT Completed"));
	}

	@Test
	public void fluxTestWIthoutError() {
		System.out.println("\n************************* - 1) Normal stringFlux \n");
		Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring");
	}
	
	@Test
	public void coldPublisherTest() throws InterruptedException {
		System.out.println("\n************************* - 1) ColdFlux \n");
		Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F").delayElements(Duration.ofSeconds(1));
		stringFlux.subscribe(s->System.out.println("Subscriber 1 : " +s));//Starts from beginning value of flux
		Thread.sleep(2000);
		stringFlux.subscribe(s->System.out.println("Subscriber 2 : " +s));//Starts from beginning value of flux
		Thread.sleep(4000);
	}
	
	@Test
	public void hotPublisherTest() throws InterruptedException {
		System.out.println("\n************************* - 1) HotFlux \n");
		Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F").delayElements(Duration.ofSeconds(1));
		ConnectableFlux<String> connectableFlux = stringFlux.publish();
		connectableFlux.connect();
		connectableFlux.subscribe(s->System.out.println("Subscriber 1 : " +s));//Starts from beginning value of flux
		Thread.sleep(3000);
		connectableFlux.subscribe(s->System.out.println("Subscriber 2 : " +s));//Starts from where it got subscribed
		Thread.sleep(4000);
	}
	
}
