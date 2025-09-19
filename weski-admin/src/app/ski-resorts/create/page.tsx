'use client';

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import {
  Form,
  Input,
  Select,
  DatePicker,
  Button,
  Card,
  message,
  Breadcrumb,
  Typography,
  Row,
  Col,
  Space,
} from 'antd';
import { ArrowLeftOutlined, SaveOutlined } from '@ant-design/icons';
import { skiResortApi } from '@/api/skiResortApi';
import type { CreateSkiResortRequest } from '@/types/skiResort';

const { Title } = Typography;
const { Option } = Select;

export default function CreateSkiResortPage() {
  const router = useRouter();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  // 폼 제출 처리
  const handleSubmit = async (values: any) => {
    setLoading(true);
    try {
      const requestData: CreateSkiResortRequest = {
        ...values,
        openingDate: values.openingDate?.format('YYYY-MM-DD'),
        closingDate: values.closingDate?.format('YYYY-MM-DD'),
      };

      await skiResortApi.createSkiResort(requestData);
      message.success('스키장이 성공적으로 생성되었습니다');
      router.push('/ski-resorts');
    } catch (error: any) {
      console.error('스키장 생성 실패:', error);
      message.error(error.response?.data?.message || '스키장 생성에 실패했습니다');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      {/* 페이지 헤더 */}
      <div className="page-header">
        <Breadcrumb style={{ marginBottom: 8 }}>
          <Breadcrumb.Item>관리자</Breadcrumb.Item>
          <Breadcrumb.Item 
            onClick={() => router.push('/ski-resorts')} 
            className="cursor-pointer"
          >
            스키장 관리
          </Breadcrumb.Item>
          <Breadcrumb.Item>새 스키장 추가</Breadcrumb.Item>
        </Breadcrumb>
        <Title level={2} style={{ margin: 0 }}>
          새 스키장 추가
        </Title>
      </div>

      {/* 메인 컨텐츠 */}
      <Card className="content-card">
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
          requiredMark={false}
          scrollToFirstError
        >
          <Row gutter={24}>
            {/* 기본 정보 */}
            <Col xs={24} lg={12}>
              <Title level={4}>기본 정보</Title>
              
              <Form.Item
                name="name"
                label="스키장명"
                rules={[{ required: true, message: '스키장명을 입력해주세요' }]}
              >
                <Input placeholder="예: 하이원 스키장" />
              </Form.Item>

              <Form.Item
                name="status"
                label="운영 상태"
                rules={[{ required: true, message: '운영 상태를 선택해주세요' }]}
              >
                <Select placeholder="운영 상태 선택">
                  <Option value="운영중">운영중</Option>
                  <Option value="운영종료">운영종료</Option>
                  <Option value="예정">예정</Option>
                </Select>
              </Form.Item>

              <Row gutter={16}>
                <Col span={12}>
                  <Form.Item name="openingDate" label="개장일">
                    <DatePicker
                      style={{ width: '100%' }}
                      placeholder="개장일 선택"
                      format="YYYY-MM-DD"
                    />
                  </Form.Item>
                </Col>
                <Col span={12}>
                  <Form.Item name="closingDate" label="폐장일">
                    <DatePicker
                      style={{ width: '100%' }}
                      placeholder="폐장일 선택"
                      format="YYYY-MM-DD"
                    />
                  </Form.Item>
                </Col>
              </Row>
            </Col>

            {/* 운영 시간 정보 */}
            <Col xs={24} lg={12}>
              <Title level={4}>운영 시간</Title>
              
              <Form.Item name="dayOperatingHours" label="주간 운영시간">
                <Input placeholder="예: 09:00~16:00" />
              </Form.Item>

              <Form.Item name="nightOperatingHours" label="야간 운영시간">
                <Input placeholder="예: 18:00~22:00" />
              </Form.Item>

              <Form.Item name="lateNightOperatingHours" label="심야 운영시간">
                <Input placeholder="예: 22:00~24:00" />
              </Form.Item>

              <Form.Item name="dawnOperatingHours" label="새벽 운영시간">
                <Input placeholder="예: 05:00~07:00" />
              </Form.Item>

              <Form.Item name="midnightOperatingHours" label="자정 운영시간">
                <Input placeholder="예: 00:00~03:00" />
              </Form.Item>

              <Form.Item name="snowfallTime" label="정설 시간">
                <Input placeholder="예: 16:00~18:00" />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={24}>
            {/* 위치 정보 */}
            <Col xs={24} lg={12}>
              <Title level={4}>위치 정보</Title>
              
              <Row gutter={16}>
                <Col span={12}>
                  <Form.Item
                    name="xCoordinate"
                    label="X 좌표 (위도 매핑값)"
                    rules={[{ required: true, message: 'X 좌표를 입력해주세요' }]}
                  >
                    <Input placeholder="예: 92" />
                  </Form.Item>
                </Col>
                <Col span={12}>
                  <Form.Item
                    name="yCoordinate"
                    label="Y 좌표 (경도 매핑값)"
                    rules={[{ required: true, message: 'Y 좌표를 입력해주세요' }]}
                  >
                    <Input placeholder="예: 120" />
                  </Form.Item>
                </Col>
              </Row>

              <Form.Item
                name="detailedAreaCode"
                label="세부 지역 코드"
                rules={[{ required: true, message: '세부 지역 코드를 입력해주세요' }]}
              >
                <Input placeholder="예: 11D10502" />
              </Form.Item>

              <Form.Item
                name="broadAreaCode"
                label="광역 지역 코드"
                rules={[{ required: true, message: '광역 지역 코드를 입력해주세요' }]}
              >
                <Input placeholder="예: 11D10000" />
              </Form.Item>
            </Col>

            {/* 도움말 */}
            <Col xs={24} lg={12}>
              <Title level={4}>입력 가이드</Title>
              <Card size="small" style={{ backgroundColor: '#f6ffed' }}>
                <p><strong>운영 시간 형식:</strong></p>
                <ul>
                  <li>시작시간~종료시간 (예: 09:00~16:00)</li>
                  <li>24시간 형식 사용</li>
                </ul>
                
                <p><strong>좌표 정보:</strong></p>
                <ul>
                  <li>기상청 격자 좌표 시스템 사용</li>
                  <li>X: 위도 매핑값, Y: 경도 매핑값</li>
                </ul>
                
                <p><strong>지역 코드:</strong></p>
                <ul>
                  <li>기상청 예보구역 코드</li>
                  <li>세부: 상세 예보용, 광역: 광역 예보용</li>
                </ul>
              </Card>
            </Col>
          </Row>

          {/* 폼 액션 버튼 */}
          <div className="form-actions">
            <Space>
              <Button
                icon={<ArrowLeftOutlined />}
                onClick={() => router.push('/ski-resorts')}
              >
                취소
              </Button>
              <Button
                type="primary"
                htmlType="submit"
                loading={loading}
                icon={<SaveOutlined />}
              >
                스키장 생성
              </Button>
            </Space>
          </div>
        </Form>
      </Card>
    </div>
  );
}
