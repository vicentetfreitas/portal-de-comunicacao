import { ApiError } from "@/types/api";

export type HttpErrorHandler = (error: ApiError) => void;

let globalHttpErrorHandler: HttpErrorHandler | null = null;

/**
 * Registers infrastructure-level HTTP error handler (e.g. toast in PKG-FE-S0-08).
 */
export function setGlobalHttpErrorHandler(
  handler: HttpErrorHandler | null
): void {
  globalHttpErrorHandler = handler;
}

export function dispatchHttpError(error: unknown): ApiError {
  const apiError =
    error instanceof ApiError ? error : ApiError.fromUnknown(error);
  globalHttpErrorHandler?.(apiError);
  return apiError;
}

export function getDefaultHttpErrorMessage(error: ApiError): string {
  switch (error.category) {
    case "validation":
      return error.message;
    case "authentication":
      return "Sessão expirada ou não autenticada.";
    case "authorization":
      return "Você não possui permissão para esta operação.";
    case "not_found":
      return "Recurso não encontrado.";
    case "conflict":
      return "Conflito ao processar a requisição.";
    case "server":
      return "Erro interno do servidor.";
    case "network":
      return "Falha de comunicação com o servidor.";
    default:
      return error.message || "Erro inesperado.";
  }
}
