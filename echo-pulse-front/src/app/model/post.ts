export interface PostMessage {
  username: string,
  content: string,
  timestamp: Date
  channelId?: {value: string}
}
