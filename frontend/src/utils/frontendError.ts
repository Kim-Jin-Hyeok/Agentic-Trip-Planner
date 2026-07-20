export function describeFrontendError(error: unknown): string {
  if (error instanceof Error) {
    const message = error.message.trim();
    return message.length > 0 ? `${error.name}: ${message}` : error.name;
  }

  if (typeof error === 'string') {
    return error.trim().length > 0 ? error.trim() : 'Unknown frontend error';
  }

  try {
    return String(error);
  } catch {
    return 'Unknown frontend error';
  }
}
