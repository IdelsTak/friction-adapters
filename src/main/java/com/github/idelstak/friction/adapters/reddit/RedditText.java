package com.github.idelstak.friction.adapters.reddit;

import java.time.*;
import java.util.*;

public record RedditText(String permalink, String body, Optional<String> author, Optional<Instant> createdAt) {
}
