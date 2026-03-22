import { useState, useEffect } from 'react';
import { getTranslationHistory } from '../services/api';

export default function HistoryPage() {
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchHistory();
  }, [page]);

  const fetchHistory = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await getTranslationHistory(page, 10);
      const data = response.data.data;
      setHistory(data.content || []);
      setTotalPages(data.totalPages || 0);
    } catch (err) {
      setError('Failed to load translation history');
      console.error('Failed to load history:', err);
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  const getLanguageName = (code) => {
    const langMap = {
      en: 'English', es: 'Spanish', fr: 'French', de: 'German',
      it: 'Italian', pt: 'Portuguese', ja: 'Japanese', ko: 'Korean',
      zh: 'Chinese', hi: 'Hindi', ar: 'Arabic', ru: 'Russian',
      nl: 'Dutch', sv: 'Swedish', tr: 'Turkish', pl: 'Polish',
      th: 'Thai', vi: 'Vietnamese', bn: 'Bengali', ta: 'Tamil',
    };
    return langMap[code] || code;
  };

  return (
    <div className="history-page">
      <div className="history-header">
        <h1>Translation History</h1>
        <p>Your past translations</p>
      </div>

      {error && <div className="alert alert-error">{error}</div>}

      {loading ? (
        <div className="loading-screen">
          <div className="spinner"></div>
          <p>Loading history...</p>
        </div>
      ) : history.length === 0 ? (
        <div className="empty-state">
          <div className="empty-icon">📝</div>
          <h2>No translations yet</h2>
          <p>Your translation history will appear here after you translate some text.</p>
        </div>
      ) : (
        <>
          <div className="history-list">
            {history.map((item, idx) => (
              <div key={item.id || idx} className="history-card">
                <div className="history-card-header">
                  <div className="history-lang-badge">
                    <span className="lang-tag">{getLanguageName(item.sourceLanguage)}</span>
                    <span className="lang-arrow">→</span>
                    <span className="lang-tag">{getLanguageName(item.targetLanguage)}</span>
                  </div>
                  <span className="history-date">{formatDate(item.createdAt)}</span>
                </div>
                <div className="history-card-body">
                  <div className="history-text-group">
                    <span className="text-label">Original</span>
                    <p className="history-text source">{item.sourceText}</p>
                  </div>
                  <div className="history-text-group">
                    <span className="text-label">Translation</span>
                    <p className="history-text translated">{item.translatedText}</p>
                  </div>
                </div>
                {item.confidenceScore && (
                  <div className="history-card-footer">
                    <span className="confidence-badge">
                      🎯 {item.confidenceScore}% confidence
                    </span>
                    {item.modelVersion && (
                      <span className="model-badge">⚡ {item.modelVersion}</span>
                    )}
                  </div>
                )}
              </div>
            ))}
          </div>

          {totalPages > 1 && (
            <div className="pagination">
              <button
                className="btn btn-outline btn-sm"
                onClick={() => setPage((p) => Math.max(0, p - 1))}
                disabled={page === 0}
              >
                ← Previous
              </button>
              <span className="page-info">
                Page {page + 1} of {totalPages}
              </span>
              <button
                className="btn btn-outline btn-sm"
                onClick={() => setPage((p) => Math.min(totalPages - 1, p + 1))}
                disabled={page >= totalPages - 1}
              >
                Next →
              </button>
            </div>
          )}
        </>
      )}
    </div>
  );
}
