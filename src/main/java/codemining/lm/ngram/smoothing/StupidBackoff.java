package codemining.lm.ngram.smoothing;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import codemining.lm.ILanguageModel;
import codemining.lm.ngram.AbstractNGramLM;
import codemining.lm.ngram.NGram;

/**
 * The stupid backoff as in the following paper:
 * 
 * inproceedings{brants2007large, title={Large language models in machine
 * translation}, author={Brants, T. and Popat, A.C. and Xu, P. and Och, F.J. and
 * Dean, J.}, booktitle={In EMNLP}, year={2007}, organization={Citeseer} }
 * 
 * @author Miltos Allamanis <m.allamanis@ed.ac.uk>
 * 
 */
public class StupidBackoff extends AbstractNGramLM {

	private static final long serialVersionUID = -4632284391688356590L;

	private static final Logger LOGGER = Logger.getLogger(StupidBackoff.class
			.getName());

	private StupidBackoff() {
		super();
	}

	public StupidBackoff(AbstractNGramLM original) {
		super(original);
	}

	@Override
	public void addFromSentence(List<String> sentence, boolean addNewVoc) {
		throw new UnsupportedOperationException(
				"StupidBackoff is an immutable Language Model");
	}

	@Override
	protected void addNgramToDict(NGram<String> ngram, boolean addNewVoc) {
		throw new UnsupportedOperationException(
				"StupidBackoff is an immutable Language Model");
	}

	@Override
	public void addSentences(Collection<List<String>> sentenceSet,
			boolean addNewVocabulary) {
		throw new UnsupportedOperationException(
				"StupidBackoff is an immutable Language Model");
	}

	@Override
	public void cutoffRare(int threshold) {
		throw new UnsupportedOperationException(
				"StupidBackoff is an immutable Language Model");
	}

	@Override
	public ILanguageModel getImmutableVersion() {
		return this;
	}

	/**
	 * Get the probability using the ngram. It assumes that the last word w_i
	 * exists in the dictionary (or has been converted to UNK).
	 * 
	 * @param ngram
	 * @return
	 */
	@Override
	public double getProbabilityFor(final NGram<String> ngram) {
		checkNotNull(ngram);
		final long thisNgramCount = trie.getCount(ngram, ngram.size() == 1,
				true);

		if (thisNgramCount > 0) {
			final long productionCount = trie.getCount(ngram.getPrefix(),
					ngram.size() == 0, false);
			checkArgument(productionCount >= thisNgramCount);

			final double mlProb = ((double) thisNgramCount)
					/ ((double) productionCount);
			checkArgument(!Double.isInfinite(mlProb));
			return mlProb;
		} else {
			checkArgument(ngram.size() > 1);
			return 0.4 * getProbabilityFor(ngram.getSuffix());

		}
	}

	@Override
	public void trainIncrementalModel(Collection<File> files)
			throws IOException {
		throw new UnsupportedOperationException(
				"StupidBackoff is an immutable Language Model");

	}

	@Override
	public void trainModel(Collection<File> files) throws IOException {
		throw new UnsupportedOperationException(
				"StupidBackoff is an immutable Language Model");
	}
}