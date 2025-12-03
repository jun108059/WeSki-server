'use client'

import React, { useState, useEffect } from 'react'
import { useParams, useRouter } from 'next/navigation'
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
  Spin,
  Alert,
  Table,
  Tag,
  Modal,
  Switch,
} from 'antd'
import { ArrowLeftOutlined, SaveOutlined, EditOutlined } from '@ant-design/icons'
import dayjs from 'dayjs'
import { skiResortApi } from '@/api/skiResortApi'
import type {
  AdminSkiResortResponse,
  UpdateSkiResortRequest,
  Slope,
  UpdateSlopeRequest,
} from '@/types/skiResort'

const { Title, Text } = Typography
const { Option } = Select

export default function SkiResortDetailPage() {
  const params = useParams()
  const router = useRouter()
  const [form] = Form.useForm()
  const [slopeForm] = Form.useForm()
  const [loading, setLoading] = useState(false)
  const [saveLoading, setSaveLoading] = useState(false)
  const [isEditing, setIsEditing] = useState(false)
  const [skiResort, setSkiResort] = useState<AdminSkiResortResponse | null>(null)
  const [slopes, setSlopes] = useState<Slope[]>([])
  const [isSlopeModalVisible, setIsSlopeModalVisible] = useState(false)
  const [editingSlope, setEditingSlope] = useState<Slope | null>(null)

  const id = params?.id as string

  // 데이터 로드
  const loadData = React.useCallback(async () => {
    if (!id) return

    setLoading(true)
    try {
      const [resortData, slopesData] = await Promise.all([
        skiResortApi.getSkiResort(Number(id)),
        skiResortApi.getSlopes(Number(id)),
      ])

      setSkiResort(resortData)
      setSlopes(slopesData)

      // 폼 데이터 설정
      form.setFieldsValue({
        ...resortData,
        openingDate: resortData.openingDate ? dayjs(resortData.openingDate) : null,
        closingDate: resortData.closingDate ? dayjs(resortData.closingDate) : null,
      })
    } catch (error: any) {
      console.error('데이터 로드 실패:', error)
      message.error(error.response?.data?.message || '스키장 정보를 불러오는데 실패했습니다')
    } finally {
      setLoading(false)
    }
  }, [id, form])

  useEffect(() => {
    loadData()
  }, [id, loadData])

  // 폼 제출 처리
  const handleSubmit = async (values: any) => {
    if (!id) return

    setSaveLoading(true)
    try {
      const requestData: UpdateSkiResortRequest = {
        ...values,
        openingDate: values.openingDate?.format('YYYY-MM-DD'),
        closingDate: values.closingDate?.format('YYYY-MM-DD'),
      }

      const updatedData = await skiResortApi.updateSkiResort(Number(id), requestData)
      setSkiResort(updatedData)
      setIsEditing(false)
      message.success('스키장 정보가 성공적으로 수정되었습니다')
    } catch (error: any) {
      console.error('스키장 수정 실패:', error)
      message.error(error.response?.data?.message || '스키장 수정에 실패했습니다')
    } finally {
      setSaveLoading(false)
    }
  }

  // 편집 취소
  const handleCancelEdit = () => {
    if (skiResort) {
      form.setFieldsValue({
        ...skiResort,
        openingDate: skiResort.openingDate ? dayjs(skiResort.openingDate) : null,
        closingDate: skiResort.closingDate ? dayjs(skiResort.closingDate) : null,
      })
    }
    setIsEditing(false)
  }

  // 슬로프 편집 모달 열기
  const showSlopeEditModal = (slope: Slope) => {
    setEditingSlope(slope)
    slopeForm.setFieldsValue({
      ...slope,
      webcamNumber: slope.webcamNo, // webcamNo를 webcamNumber로 매핑
    })
    setIsSlopeModalVisible(true)
  }

  // 슬로프 수정 제출
  const handleSlopeSubmit = async () => {
    if (!editingSlope) return

    try {
      const values = await slopeForm.validateFields()
      const requestData: UpdateSlopeRequest = {
        ...values,
      }

      await skiResortApi.updateSlope(editingSlope.slopeId, requestData)

      message.success('슬로프 정보가 수정되었습니다')
      setIsSlopeModalVisible(false)

      // 슬로프 목록 새로고침
      const updatedSlopes = await skiResortApi.getSlopes(Number(id))
      setSlopes(updatedSlopes)
    } catch (error: any) {
      console.error('슬로프 수정 실패:', error)
      message.error('슬로프 수정에 실패했습니다')
    }
  }

  // 슬로프 테이블 컬럼
  const slopeColumns = [
    {
      title: 'ID',
      dataIndex: 'slopeId',
      key: 'slopeId',
      width: 80,
    },
    {
      title: '슬로프명',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: '난이도',
      dataIndex: 'difficulty',
      key: 'difficulty',
      render: (difficulty: string) => {
        let color = 'default'
        if (difficulty === '초급') color = 'green'
        if (difficulty === '중급') color = 'blue'
        if (difficulty === '상급') color = 'red'
        if (difficulty === '최상급') color = 'purple'
        return <Tag color={color}>{difficulty}</Tag>
      },
    },
    {
      title: '주간',
      dataIndex: 'isDayOperating',
      key: 'isDayOperating',
      render: (val: boolean) => (val ? <Tag color="blue">운영</Tag> : <Tag>미운영</Tag>),
    },
    {
      title: '야간',
      dataIndex: 'isNightOperating',
      key: 'isNightOperating',
      render: (val: boolean) => (val ? <Tag color="blue">운영</Tag> : <Tag>미운영</Tag>),
    },
    {
      title: '심야',
      dataIndex: 'isLateNightOperating',
      key: 'isLateNightOperating',
      render: (val: boolean) => (val ? <Tag color="blue">운영</Tag> : <Tag>미운영</Tag>),
    },
    {
      title: '작업',
      key: 'action',
      render: (_: any, record: Slope) => (
        <Button size="small" icon={<EditOutlined />} onClick={() => showSlopeEditModal(record)}>
          수정
        </Button>
      ),
    },
  ]

  if (loading) {
    return (
      <div className="loading-container">
        <Spin size="large" />
      </div>
    )
  }

  if (!skiResort) {
    return (
      <div className="error-container">
        <Alert
          message="스키장을 찾을 수 없습니다"
          description="요청하신 스키장 정보가 존재하지 않습니다."
          type="error"
          showIcon
          action={<Button onClick={() => router.push('/ski-resorts')}>목록으로 돌아가기</Button>}
        />
      </div>
    )
  }

  return (
    <div>
      {/* 페이지 헤더 */}
      <div className="page-header">
        <Breadcrumb style={{ marginBottom: 8 }}>
          <Breadcrumb.Item>관리자</Breadcrumb.Item>
          <Breadcrumb.Item onClick={() => router.push('/ski-resorts')} className="cursor-pointer">
            스키장 관리
          </Breadcrumb.Item>
          <Breadcrumb.Item>{skiResort.name}</Breadcrumb.Item>
        </Breadcrumb>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Title level={2} style={{ margin: 0 }}>
            {skiResort.name} 상세 정보
          </Title>
          {!isEditing && (
            <Button type="primary" icon={<EditOutlined />} onClick={() => setIsEditing(true)}>
              수정하기
            </Button>
          )}
        </div>
      </div>

      {/* 메인 컨텐츠 */}
      <Card className="content-card">
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
          disabled={!isEditing}
          requiredMark={false}
          scrollToFirstError
        >
          <Row gutter={24}>
            {/* 기본 정보 */}
            <Col xs={24} lg={12}>
              <Title level={4}>기본 정보</Title>

              <Form.Item label="스키장 ID">
                <Text strong>{skiResort.resortId}</Text>
              </Form.Item>

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

              <Row gutter={16}>
                <Col span={12}>
                  <Form.Item label="운영 슬로프 수">
                    <Text>{skiResort.openSlopes}개</Text>
                  </Form.Item>
                </Col>
                <Col span={12}>
                  <Form.Item label="전체 슬로프 수">
                    <Text>{skiResort.totalSlopes}개</Text>
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

            {/* 메타데이터 */}
            <Col xs={24} lg={12}>
              <Title level={4}>메타데이터</Title>

              <Form.Item label="생성일시">
                <Text>{new Date(skiResort.createdAt).toLocaleString('ko-KR')}</Text>
              </Form.Item>

              <Form.Item label="최종 수정일시">
                <Text>{new Date(skiResort.updatedAt).toLocaleString('ko-KR')}</Text>
              </Form.Item>

              {isEditing && (
                <Card size="small" style={{ backgroundColor: '#f6ffed' }}>
                  <p>
                    <strong>수정 안내:</strong>
                  </p>
                  <ul>
                    <li>슬로프 수는 자동으로 계산됩니다</li>
                    <li>운영 상태는 날짜 기준으로 자동 업데이트 가능합니다</li>
                    <li>좌표 정보 변경 시 날씨 데이터에 영향을 줄 수 있습니다</li>
                  </ul>
                </Card>
              )}
            </Col>
          </Row>

          {/* 폼 액션 버튼 */}
          <div className="form-actions">
            <Space>
              <Button icon={<ArrowLeftOutlined />} onClick={() => router.push('/ski-resorts')}>
                목록으로
              </Button>
              {isEditing ? (
                <>
                  <Button onClick={handleCancelEdit}>취소</Button>
                  <Button
                    type="primary"
                    htmlType="submit"
                    loading={saveLoading}
                    icon={<SaveOutlined />}
                  >
                    저장
                  </Button>
                </>
              ) : (
                <Button type="primary" icon={<EditOutlined />} onClick={() => setIsEditing(true)}>
                  수정하기
                </Button>
              )}
            </Space>
          </div>
        </Form>
      </Card>

      {/* 슬로프 목록 섹션 */}
      <Card className="content-card" style={{ marginTop: 24 }}>
        <div
          style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            marginBottom: 16,
          }}
        >
          <Title level={4} style={{ margin: 0 }}>
            슬로프 목록
          </Title>
        </div>

        <Table
          columns={slopeColumns}
          dataSource={slopes}
          rowKey="slopeId"
          pagination={false}
          size="small"
        />
      </Card>

      {/* 슬로프 수정 모달 */}
      <Modal
        title="슬로프 정보 수정"
        open={isSlopeModalVisible}
        onOk={handleSlopeSubmit}
        onCancel={() => setIsSlopeModalVisible(false)}
        okText="저장"
        cancelText="취소"
      >
        <Form form={slopeForm} layout="vertical">
          <Form.Item
            name="name"
            label="슬로프명"
            rules={[{ required: true, message: '슬로프명을 입력해주세요' }]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            name="difficulty"
            label="난이도"
            rules={[{ required: true, message: '난이도를 선택해주세요' }]}
          >
            <Select>
              <Option value="초급">초급</Option>
              <Option value="초중급">초중급</Option>
              <Option value="중급">중급</Option>
              <Option value="중상급">중상급</Option>
              <Option value="상급">상급</Option>
              <Option value="최상급">최상급</Option>
              <Option value="파크">파크</Option>
              <Option value="익스트림">익스트림</Option>
            </Select>
          </Form.Item>

          <Form.Item name="webcamNumber" label="웹캠 번호">
            <Input type="number" />
          </Form.Item>

          <Row gutter={16}>
            <Col span={12}>
              <Form.Item name="isDayOperating" label="주간 운영" valuePropName="checked">
                <Switch checkedChildren="운영" unCheckedChildren="미운영" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item name="isNightOperating" label="야간 운영" valuePropName="checked">
                <Switch checkedChildren="운영" unCheckedChildren="미운영" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item name="isLateNightOperating" label="심야 운영" valuePropName="checked">
                <Switch checkedChildren="운영" unCheckedChildren="미운영" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item name="isDawnOperating" label="새벽 운영" valuePropName="checked">
                <Switch checkedChildren="운영" unCheckedChildren="미운영" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item name="isMidnightOperating" label="자정 운영" valuePropName="checked">
                <Switch checkedChildren="운영" unCheckedChildren="미운영" />
              </Form.Item>
            </Col>
          </Row>
        </Form>
      </Modal>
    </div>
  )
}
