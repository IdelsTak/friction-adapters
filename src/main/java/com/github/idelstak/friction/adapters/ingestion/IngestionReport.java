package com.github.idelstak.friction.adapters.ingestion;

public record IngestionReport(int accepted, int duplicates, int rejected, int sourceFailures) {

  public IngestionReport accept() {
    return new IngestionReport(accepted + 1, duplicates, rejected, sourceFailures);
  }

  public IngestionReport duplicate() {
    return new IngestionReport(accepted, duplicates + 1, rejected, sourceFailures);
  }

  public IngestionReport reject() {
    return new IngestionReport(accepted, duplicates, rejected + 1, sourceFailures);
  }

  public IngestionReport fail() {
    return new IngestionReport(accepted, duplicates, rejected, sourceFailures + 1);
  }
}
