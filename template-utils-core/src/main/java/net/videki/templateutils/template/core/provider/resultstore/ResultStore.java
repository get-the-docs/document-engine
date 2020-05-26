package net.videki.templateutils.template.core.provider.resultstore;

import net.videki.templateutils.template.core.documentstructure.GenerationResult;

public interface ResultStore {
    void save(final GenerationResult result);
}
