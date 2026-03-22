import { useState, useEffect } from 'react';
import { translate, getLanguages } from '../services/api';

export default function TranslatePage() {
  const [sourceText, setSourceText] = useState('');
  const [translatedText, setTranslatedText] = useState('');
  const [sourceLanguage, setSourceLanguage] = useState('en');
  const [targetLanguage, setTargetLanguage] = useState('es');
  const [languages, setLanguages] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [charCount, setCharCount] = useState(0);
  const [translationInfo, setTranslationInfo] = useState(null);

  useEffect(() => {
    fetchLanguages();
  }, []);

  const fetchLanguages = async () => {
    try {
      const response = await getLanguages();
      setLanguages(response.data.data || []);
    } catch (err) {
      console.error('Failed to fetch languages:', err);
      // Fallback languages
      setLanguages([
        { code: 'en', name: 'English', nativeName: 'English' },
        { code: 'es', name: 'Spanish', nativeName: 'Español' },
        { code: 'fr', name: 'French', nativeName: 'Français' },
        { code: 'de', name: 'German', nativeName: 'Deutsch' },
        { code: 'it', name: 'Italian', nativeName: 'Italiano' },
        { code: 'pt', name: 'Portuguese', nativeName: 'Português' },
        { code: 'ja', name: 'Japanese', nativeName: '日本語' },
        { code: 'ko', name: 'Korean', nativeName: '한국어' },
        { code: 'zh', name: 'Chinese', nativeName: '中文' },
        { code: 'hi', name: 'Hindi', nativeName: 'हिन्दी' },
        { code: 'ar', name: 'Arabic', nativeName: 'العربية' },
        { code: 'ru', name: 'Russian', nativeName: 'Русский' },
      ]);
    }
  };

  const handleTranslate = async () => {
    if (!sourceText.trim()) return;
    setLoading(true);
    setError('');
    setTranslatedText('');

    try {
      const response = await translate({
        text: sourceText,
        sourceLanguage,
        targetLanguage,
        saveToHistory: true,
      });
      const data = response.data.data;
      setTranslatedText(data.translatedText);
      setTranslationInfo({
        confidence: data.confidenceScore,
        model: data.modelVersion,
        fromCache: data.fromCache,
      });
    } catch (err) {
      setError(err.response?.data?.message || 'Translation failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleSwapLanguages = () => {
    setSourceLanguage(targetLanguage);
    setTargetLanguage(sourceLanguage);
    setSourceText(translatedText);
    setTranslatedText(sourceText);
  };

  const handleSourceTextChange = (e) => {
    const text = e.target.value;
    if (text.length <= 10000) {
      setSourceText(text);
      setCharCount(text.length);
    }
  };

  const handleCopy = () => {
    navigator.clipboard.writeText(translatedText);
  };

  const handleClear = () => {
    setSourceText('');
    setTranslatedText('');
    setCharCount(0);
    setTranslationInfo(null);
    setError('');
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Enter' && (e.ctrlKey || e.metaKey)) {
      handleTranslate();
    }
  };

  return (
    <div className="translate-page">
      <div className="translate-header">
        <h1>AI Language Translator</h1>
        <p>Powered by Google Cloud Translation API</p>
      </div>

      <div className="translate-container">
        {/* Language Selector Bar */}
        <div className="language-bar">
          <div className="language-select-wrapper">
            <select
              id="source-language"
              value={sourceLanguage}
              onChange={(e) => setSourceLanguage(e.target.value)}
              className="language-select"
            >
              {languages.map((lang) => (
                <option key={`src-${lang.code}`} value={lang.code}>
                  {lang.name} ({lang.nativeName})
                </option>
              ))}
            </select>
          </div>

          <button
            className="btn-swap"
            onClick={handleSwapLanguages}
            title="Swap languages"
          >
            ⇄
          </button>

          <div className="language-select-wrapper">
            <select
              id="target-language"
              value={targetLanguage}
              onChange={(e) => setTargetLanguage(e.target.value)}
              className="language-select"
            >
              {languages.map((lang) => (
                <option key={`tgt-${lang.code}`} value={lang.code}>
                  {lang.name} ({lang.nativeName})
                </option>
              ))}
            </select>
          </div>
        </div>

        {error && <div className="alert alert-error">{error}</div>}

        {/* Translation Areas */}
        <div className="translate-panels">
          <div className="translate-panel source-panel">
            <div className="panel-header">
              <span className="panel-label">Source</span>
              <div className="panel-actions">
                <span className="char-count">{charCount}/10000</span>
                {sourceText && (
                  <button className="btn-icon" onClick={handleClear} title="Clear">
                    ✕
                  </button>
                )}
              </div>
            </div>
            <textarea
              id="source-text"
              className="translate-textarea"
              placeholder="Enter text to translate..."
              value={sourceText}
              onChange={handleSourceTextChange}
              onKeyDown={handleKeyDown}
              rows={8}
            />
            <div className="panel-footer">
              <span className="keyboard-hint">Ctrl+Enter to translate</span>
            </div>
          </div>

          <div className="translate-panel result-panel">
            <div className="panel-header">
              <span className="panel-label">Translation</span>
              <div className="panel-actions">
                {translatedText && (
                  <button className="btn-icon" onClick={handleCopy} title="Copy">
                    📋
                  </button>
                )}
              </div>
            </div>
            <div className="translate-result" id="translated-text">
              {loading ? (
                <div className="translate-loading">
                  <div className="spinner"></div>
                  <span>Translating...</span>
                </div>
              ) : translatedText ? (
                <p>{translatedText}</p>
              ) : (
                <p className="placeholder-text">Translation will appear here</p>
              )}
            </div>
            {translationInfo && (
              <div className="panel-footer translation-meta">
                <span className="meta-item">
                  🎯 {translationInfo.confidence}% confidence
                </span>
                <span className="meta-item">
                  ⚡ {translationInfo.model}
                </span>
                {translationInfo.fromCache && (
                  <span className="meta-item cached">📦 Cached</span>
                )}
              </div>
            )}
          </div>
        </div>

        {/* Translate Button */}
        <div className="translate-action">
          <button
            className="btn btn-primary btn-translate"
            onClick={handleTranslate}
            disabled={loading || !sourceText.trim()}
          >
            {loading ? (
              <span className="btn-loading">
                <span className="spinner-sm"></span> Translating...
              </span>
            ) : (
              <>🌐 Translate</>
            )}
          </button>
        </div>
      </div>

      {/* Features Section */}
      <div className="features-section">
        <div className="feature-card">
          <div className="feature-icon">🚀</div>
          <h3>Fast & Accurate</h3>
          <p>Powered by Google Cloud AI for high-quality translations</p>
        </div>
        <div className="feature-card">
          <div className="feature-icon">🌍</div>
          <h3>20+ Languages</h3>
          <p>Support for major world languages including Asian and Middle-Eastern</p>
        </div>
        <div className="feature-card">
          <div className="feature-icon">📝</div>
          <h3>History Tracking</h3>
          <p>Save and review your past translations anytime</p>
        </div>
      </div>
    </div>
  );
}
