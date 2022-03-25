package com.example.demo.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.out.Shoes;
import org.junit.jupiter.api.Test;

/**
 * Test AbstractFacade class with the example of ShoeFacade
 */
class AbstractFacadeTest {

  @Test
  void register_twoImplementationWithSameVersion_shouldRaiseIllegalArgumentException() {
    // given
    ShoeFacade facade = new ShoeFacade();
    facade.register(0, filter -> null);

    // when
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> facade.register(0, filter -> null)
        );

    // then
    assertEquals(
        "ShoeCore implementations already"
            + " contains an implementation with version 0",
        exception.getMessage()
    );
  }

  @Test
  void get_noImplementationMatchingVersion_shouldRaiseIllegalArgumentException() {
    // when
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> new ShoeFacade().get(0)
        );

    // then
    assertEquals("version 0 is not registered as a ShoeCore implementation", exception.getMessage());
  }

  @Test
  void get_versionRegistered_shouldCallProperImplementation() {
    // given
    ShoeFacade facade = new ShoeFacade();
    facade.register(0, filter -> null);
    Shoes shoesVersion1 = Shoes.builder().build();
    facade.register(1, filter -> shoesVersion1);

    // when
    Shoes resultVersion1 = facade.get(1).search(new ShoeFilter(null, null));
    Shoes resultVersion0 = facade.get(0).search(new ShoeFilter(null, null));

    // then
    assertEquals(shoesVersion1, resultVersion1);
    assertNull(resultVersion0);
  }

}
