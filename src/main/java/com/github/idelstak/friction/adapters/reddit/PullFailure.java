package com.github.idelstak.friction.adapters.reddit;

import java.util.*;

public sealed interface PullFailure {

  String message();

  Optional<Throwable> cause();

  record Transient(String message, Optional<Throwable> cause) implements PullFailure {
  }

  record Terminal(String message, Optional<Throwable> cause) implements PullFailure {
  }
}
