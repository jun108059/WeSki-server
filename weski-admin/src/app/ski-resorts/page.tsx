'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import {
  Table,
  Button,
  Space,
  Tag,
  message,
  Popconfirm,
  Card,
  Breadcrumb,
  Typography,
  Input,
  Select,
  Alert, BreadcrumbProps,
} from 'antd';
import {
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  ReloadOutlined,
  SearchOutlined,
  SyncOutlined,
} from '@ant-design/icons';
import type { ColumnsType } from 'antd/es/table';
import { skiResortApi } from '@/api/skiResortApi';
import type { AdminSkiResortResponse } from '@/types/skiResort';
import Link from "next/link";

const { Title } = Typography;
const { Search } = Input;
const { Option } = Select;

export default function SkiResortListPage() {
  const router = useRouter();
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<AdminSkiResortResponse[]>([]);
  const [filteredData, setFilteredData] = useState<AdminSkiResortResponse[]>([]);
  const [searchText, setSearchText] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('all');
  const [batchLoading, setBatchLoading] = useState(false);

  const breadcrumbItems: BreadcrumbProps['items'] = [
    { title: <Link href="/admin">관리자</Link> }, // 링크가 필요 없다면 문자열로만 넣어도 됩니다.
    { title: '스키장 관리' },
  ];

  // 데이터 로드
  const loadData = async () => {
    setLoading(true);
    try {
      const resorts = await skiResortApi.getAllSkiResorts();
      setData(resorts);
      setFilteredData(resorts);
      message.success('스키장 목록을 불러왔습니다');
    } catch (error) {
      console.error('데이터 로드 실패:', error);
      message.error('스키장 목록을 불러오는데 실패했습니다');
    } finally {
      setLoading(false);
    }
  };

  // 컴포넌트 마운트 시 데이터 로드
  useEffect(() => {
    loadData();
  }, []);

  // 필터링 로직
  useEffect(() => {
    let filtered = data;

    // 텍스트 검색
    if (searchText) {
      filtered = filtered.filter((resort) =>
        resort.name.toLowerCase().includes(searchText.toLowerCase())
      );
    }

    // 상태 필터
    if (statusFilter !== 'all') {
      filtered = filtered.filter((resort) => resort.status === statusFilter);
    }

    setFilteredData(filtered);
  }, [data, searchText, statusFilter]);

  // 스키장 삭제
  const handleDelete = async (resortId: number) => {
    try {
      await skiResortApi.deleteSkiResort(resortId);
      message.success('스키장이 삭제되었습니다');
      loadData();
    } catch (error: any) {
      console.error('삭제 실패:', error);
      message.error(error.response?.data?.message || '스키장 삭제에 실패했습니다');
    }
  };

  // 일괄 상태 업데이트
  const handleBatchUpdateStatus = async () => {
    setBatchLoading(true);
    try {
      await skiResortApi.updateAllResortStatus();
      message.success('모든 스키장 상태가 업데이트되었습니다');
      loadData();
    } catch (error) {
      console.error('일괄 상태 업데이트 실패:', error);
      message.error('상태 업데이트에 실패했습니다');
    } finally {
      setBatchLoading(false);
    }
  };

  // 일괄 슬로프 수 업데이트
  const handleBatchUpdateSlopes = async () => {
    setBatchLoading(true);
    try {
      await skiResortApi.updateAllSlopeCount();
      message.success('모든 스키장 슬로프 수가 업데이트되었습니다');
      loadData();
    } catch (error) {
      console.error('일괄 슬로프 수 업데이트 실패:', error);
      message.error('슬로프 수 업데이트에 실패했습니다');
    } finally {
      setBatchLoading(false);
    }
  };

  // 상태별 태그 색상
  const getStatusColor = (status: string): string => {
    switch (status) {
      case '운영중':
        return 'green';
      case '운영종료':
        return 'red';
      case '예정':
        return 'blue';
      default:
        return 'default';
    }
  };

  // 테이블 컬럼 정의
  const columns: ColumnsType<AdminSkiResortResponse> = [
    {
      title: 'ID',
      dataIndex: 'resortId',
      key: 'resortId',
      width: 80,
      sorter: (a, b) => a.resortId - b.resortId,
    },
    {
      title: '스키장명',
      dataIndex: 'name',
      key: 'name',
      sorter: (a, b) => a.name.localeCompare(b.name),
      render: (text: string, record) => (
        <Button
          type="link"
          onClick={() => router.push(`/ski-resorts/${record.resortId}`)}
          style={{ padding: 0, height: 'auto' }}
        >
          {text}
        </Button>
      ),
    },
    {
      title: '운영상태',
      dataIndex: 'status',
      key: 'status',
      width: 100,
      filters: [
        { text: '운영중', value: '운영중' },
        { text: '운영종료', value: '운영종료' },
        { text: '예정', value: '예정' },
      ],
      onFilter: (value, record) => record.status === value,
      render: (status: string) => (
        <Tag color={getStatusColor(status)} className={`resort-status-${status}`}>
          {status}
        </Tag>
      ),
    },
    {
      title: '개장일',
      dataIndex: 'openingDate',
      key: 'openingDate',
      width: 120,
      sorter: (a, b) => {
        const dateA = a.openingDate || '';
        const dateB = b.openingDate || '';
        return dateA.localeCompare(dateB);
      },
      render: (date: string) => date || '미정',
    },
    {
      title: '폐장일',
      dataIndex: 'closingDate',
      key: 'closingDate',
      width: 120,
      render: (date: string) => date || '미정',
    },
    {
      title: '슬로프',
      key: 'slopes',
      width: 100,
      render: (_, record) => (
        <span>
          {record.openSlopes}/{record.totalSlopes}
        </span>
      ),
    },
    {
      title: '수정일',
      dataIndex: 'updatedAt',
      key: 'updatedAt',
      width: 180,
      sorter: (a, b) => new Date(a.updatedAt).getTime() - new Date(b.updatedAt).getTime(),
      render: (date: string) => new Date(date).toLocaleString('ko-KR'),
    },
    {
      title: '작업',
      key: 'actions',
      width: 120,
      render: (_, record) => (
        <Space size="small">
          <Button
            type="primary"
            size="small"
            icon={<EditOutlined />}
            onClick={() => router.push(`/ski-resorts/${record.resortId}`)}
          >
            수정
          </Button>
          <Popconfirm
            title="스키장 삭제"
            description="정말로 이 스키장을 삭제하시겠습니까?"
            onConfirm={() => handleDelete(record.resortId)}
            okText="삭제"
            cancelText="취소"
          >
            <Button type="primary" danger size="small" icon={<DeleteOutlined />}>
              삭제
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <div>
      {/* 페이지 헤더 */}
      <div className="page-header">
        <Breadcrumb
            style={{ marginBottom: 8 }}
            items={breadcrumbItems}
        />
        <Title level={2} style={{ margin: 0 }}>
          스키장 관리
        </Title>
      </div>

      {/* 메인 컨텐츠 */}
      <Card className="content-card">
        {/* 액션 버튼들 */}
        <div className="table-actions">
          <Space>
            <Button
              type="primary"
              icon={<PlusOutlined />}
              onClick={() => router.push('/ski-resorts/create')}
            >
              새 스키장 추가
            </Button>
            <Button
              icon={<ReloadOutlined />}
              onClick={loadData}
              loading={loading}
            >
              새로고침
            </Button>
            <Button
              icon={<SyncOutlined />}
              onClick={handleBatchUpdateStatus}
              loading={batchLoading}
            >
              상태 일괄 업데이트
            </Button>
            <Button
              icon={<SyncOutlined />}
              onClick={handleBatchUpdateSlopes}
              loading={batchLoading}
            >
              슬로프 수 업데이트
            </Button>
          </Space>

          <Space>
            <Search
              placeholder="스키장명 검색"
              allowClear
              enterButton={<SearchOutlined />}
              style={{ width: 250 }}
              value={searchText}
              onChange={(e) => setSearchText(e.target.value)}
            />
            <Select
              style={{ width: 120 }}
              value={statusFilter}
              onChange={setStatusFilter}
            >
              <Option value="all">전체 상태</Option>
              <Option value="운영중">운영중</Option>
              <Option value="운영종료">운영종료</Option>
              <Option value="예정">예정</Option>
            </Select>
          </Space>
        </div>

        {/* 통계 정보 */}
        {data.length > 0 && (
          <Alert
            message={
              <span>
                총 <strong>{data.length}</strong>개 스키장 |
                운영중 <strong>{data.filter(r => r.status === '운영중').length}</strong>개 |
                예정 <strong>{data.filter(r => r.status === '예정').length}</strong>개 |
                운영종료 <strong>{data.filter(r => r.status === '운영종료').length}</strong>개
                {filteredData.length !== data.length && (
                  <span> | 필터링된 결과: <strong>{filteredData.length}</strong>개</span>
                )}
              </span>
            }
            type="info"
            showIcon
            style={{ marginBottom: 16 }}
          />
        )}

        {/* 데이터 테이블 */}
        <Table
          columns={columns}
          dataSource={filteredData}
          rowKey="resortId"
          loading={loading}
          pagination={{
            total: filteredData.length,
            pageSize: 10,
            showSizeChanger: true,
            showQuickJumper: true,
            showTotal: (total, range) =>
              `${range[0]}-${range[1]} / 총 ${total}개`,
          }}
          scroll={{ x: 800 }}
        />
      </Card>
    </div>
  );
}
