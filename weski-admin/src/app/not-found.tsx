import { Button, Result } from 'antd';
import Link from 'next/link';

export default function NotFound() {
  return (
    <div style={{ padding: '50px 0' }}>
      <Result
        status="404"
        title="404"
        subTitle="죄송합니다. 요청하신 페이지를 찾을 수 없습니다."
        extra={
          <Link href="/ski-resorts">
            <Button type="primary">스키장 목록으로 돌아가기</Button>
          </Link>
        }
      />
    </div>
  );
}
