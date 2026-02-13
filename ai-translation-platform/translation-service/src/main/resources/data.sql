-- Initialize supported languages
INSERT INTO languages (code, name, native_name, active, supports_text_translation, supports_speech_recognition, supports_speech_synthesis) 
VALUES 
    ('en', 'English', 'English', true, true, true, true),
    ('es', 'Spanish', 'Español', true, true, true, true),
    ('fr', 'French', 'Français', true, true, true, true),
    ('de', 'German', 'Deutsch', true, true, true, true),
    ('it', 'Italian', 'Italiano', true, true, true, true),
    ('pt', 'Portuguese', 'Português', true, true, true, true),
    ('ru', 'Russian', 'Русский', true, true, false, false),
    ('zh', 'Chinese', '中文', true, true, true, true),
    ('ja', 'Japanese', '日本語', true, true, true, true),
    ('ko', 'Korean', '한국어', true, true, true, true),
    ('ar', 'Arabic', 'العربية', true, true, false, false),
    ('hi', 'Hindi', 'हिन्दी', true, true, true, true)
ON CONFLICT (code) DO NOTHING;
