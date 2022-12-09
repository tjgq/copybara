/*
 * Copyright (C) 2022 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.copybara.onboard.core;

import static com.google.common.truth.Truth.assertThat;
import static com.google.copybara.onboard.core.InputProvider.DEFAULT_PRIORITY;
import static org.junit.Assert.assertThrows;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MapBasedInputProviderTest {

  public static final InputProviderResolver RESOLVER = new InputProviderResolver() {

    @Override
    public <T> T resolve(Input<T> input) {
      throw new IllegalStateException("Shouldn't be called in this test!");
    }
  };
  private static final Input<String> INPUT = Input.create("InputProviderResolver",
      "just for test", null, String.class, (s, resolver) -> s);

  private static final Input<Integer> OTHER = Input.create("InputProviderResolverOther",
      "just for test", null, Integer.class, (s, resolver) -> Integer.valueOf(s));

  @Test
  public void testSimple() throws CannotProvideException, InterruptedException {
    MapBasedInputProvider provider = new MapBasedInputProvider(
        ImmutableMap.of(INPUT.name(), "hello", OTHER.name(), "42"), DEFAULT_PRIORITY);

    assertThat(provider.provides())
        .containsExactly(INPUT, DEFAULT_PRIORITY, OTHER, DEFAULT_PRIORITY);
    assertThat(provider.resolve(INPUT, RESOLVER)).isEqualTo(Optional.of("hello"));
    assertThat(provider.resolve(OTHER, RESOLVER)).isEqualTo(Optional.of(42));
  }

  @Test
  public void testWrongValue() throws CannotProvideException {
    MapBasedInputProvider provider = new MapBasedInputProvider(
        ImmutableMap.of(OTHER.name(), "wrong"), DEFAULT_PRIORITY);

    assertThat(provider.provides()).containsExactly(OTHER, DEFAULT_PRIORITY);
    assertThat(assertThrows(CannotProvideException.class,
        () -> provider.resolve(OTHER, RESOLVER))).hasMessageThat().contains("Invalid value");
  }
}
